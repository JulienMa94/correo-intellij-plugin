package com.github.julienma94.intellijplugintest.ui.subscribe

import com.github.julienma94.intellijplugintest.core.services.subscribe.SubscribeService
import com.github.julienma94.intellijplugintest.ui.common.DefaultPanel
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.DumbAwareAction
import org.correomqtt.core.model.Qos
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.core.pubsub.SubscribeEvent
import org.correomqtt.core.pubsub.UnsubscribeEvent
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Observes
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

@DefaultBean
class SubscribeView {
    private val subscribeService = service<SubscribeService>()
    private val content: JPanel = JPanel(BorderLayout())
    private val messageContainer: JPanel = JPanel()

    private val messages: MutableMap<String, MutableList<IncomingMessageEvent>> = mutableMapOf()
    private val displayedTopics = mutableSetOf<String>();

    init {
        messageContainer.layout = BoxLayout(messageContainer, BoxLayout.Y_AXIS)
        messageContainer.border = EmptyBorder(0, 0, 0, 0)
        updateAccordion()
    }

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Received incoming message event for topic ${event.messageDTO.topic}")
        addMessageToMap(event)
        updateAccordion()
    }

    private fun addMessageToMap(message: IncomingMessageEvent) {
        val topic = message.messageDTO.topic
        val currentMessages = messages.getOrDefault(topic, mutableListOf())
        val newMessage = mutableListOf(message)

        val newMessages = newMessage + currentMessages
        messages[topic] = newMessages.toMutableList()
    }

    fun onSubscribeToTopic(@Observes event: SubscribeEvent) {
        println("Received subscription event for topic ${event.subscriptionDTO.topic}")
        updateAccordion()
    }

    fun onUnsubscribe(@Observes event: UnsubscribeEvent) {
        println("Received unsubscribe event for topic ${event.subscriptionDTO.topic}")
        messages.remove(event.subscriptionDTO.topic)
        updateAccordion()
    }

    fun getSubscribeContent(): JPanel {
        content.layout = BorderLayout()

        val rowScrollPane = JScrollPane(messageContainer)
        rowScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        rowScrollPane.preferredSize = Dimension(content.width, content.height)
        rowScrollPane.border = null

        content.add(getSubscribeSection(), BorderLayout.NORTH)
        content.add(rowScrollPane, BorderLayout.CENTER)
        return content
    }

    private fun getSubscribeSection(): JPanel {

        val subscribeSection = JPanel(BorderLayout())

        // Subscribe Section
        val textField = JTextField()
        val comboBox = JComboBox(arrayOf(Qos.AT_MOST_ONCE, Qos.AT_LEAST_ONCE, Qos.EXACTLY_ONCE))
        comboBox.preferredSize = Dimension(20, comboBox.preferredSize.height) // Set fixed width
        comboBox.maximumSize = Dimension(20, comboBox.preferredSize.height) // Set fixed width

        val subscribeAction = object : DumbAwareAction(
            "Subscribing to ${textField.text}",
            "Subscribing action",
            AllIcons.Actions.Execute
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                val topic = textField.text
                subscribeService.subscribe(topic, comboBox.selectedItem as Qos)
                if (!messages.containsKey(topic)) {
                    messages[topic] = mutableListOf()
                }
                updateAccordion()
            }
        }
        val subscribeButton = JButton("Subscribe")
        subscribeButton.addActionListener {
            subscribeAction.actionPerformed(
                AnActionEvent.createFromAnAction(
                    subscribeAction,
                    null,
                    "",
                    DataContext.EMPTY_CONTEXT
                )
            )
        }

        subscribeButton.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    subscribeAction.actionPerformed(
                        AnActionEvent.createFromAnAction(
                            subscribeAction,
                            null,
                            "",
                            DataContext.EMPTY_CONTEXT
                        )
                    )
                }
            }
        })

        // Add KeyListener to textField to detect Enter key press
        textField.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    subscribeAction.actionPerformed(
                        AnActionEvent.createFromAnAction(
                            subscribeAction,
                            null,
                            "",
                            DataContext.EMPTY_CONTEXT
                        )
                    )
                }
            }
        })

        // Set the layout and constraints
        val gridBagLayout = GridBagLayout()
        val constraints = GridBagConstraints()
        val panel = JPanel(gridBagLayout)

        // Add the JTextField to the panel
        constraints.weightx = 0.7
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        panel.add(textField, constraints)

        constraints.gridx = 1
        constraints.weightx = 0.1
        panel.add(comboBox, constraints)

        constraints.gridx = 2
        constraints.weightx = 0.2
        panel.add(subscribeButton, constraints)

        subscribeSection.add(panel, BorderLayout.NORTH)

        return subscribeSection
    }

    private fun updateAccordion() {
        messageContainer.removeAll() // Clear the container

        if (messages.isEmpty()) {
            val defaultPanel = DefaultPanel().getContent("Subscribe to a topic to see received messages")
            messageContainer.add(defaultPanel)
        } else {
            val colorsScheme = EditorColorsManager.getInstance().globalScheme

            for ((topic, messages) in messages) {
                val topicPanel = JPanel(BorderLayout())
                topicPanel.border = null

                // Accordion Header Panel
                val headerPanel = JPanel(BorderLayout())
                headerPanel.border = BorderFactory.createMatteBorder(0, 0, 1, 0, colorsScheme.defaultBackground)
                headerPanel.preferredSize = Dimension(content.width, 50)
                headerPanel.maximumSize = Dimension(Int.MAX_VALUE, 50)
                headerPanel.minimumSize = Dimension(content.width, 50)

                val topicLabel = JLabel(topic)
                topicLabel.font = topicLabel.font.deriveFont(Font.BOLD)

                val countLabel = JLabel(messages.size.toString())
                countLabel.border = EmptyBorder(5, 5, 5, 5)

                headerPanel.add(topicLabel, BorderLayout.WEST)
                headerPanel.add(countLabel, BorderLayout.CENTER)

                topicPanel.add(headerPanel, BorderLayout.NORTH)

                val messagePanel = JPanel()
                messagePanel.layout = BoxLayout(messagePanel, BoxLayout.Y_AXIS)
                messagePanel.isVisible = displayedTopics.contains(topic)

                val messageItem = MessageItem()

                headerPanel.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent?) {
                        messagePanel.isVisible = !messagePanel.isVisible

                        if (messagePanel.isVisible) {
                            displayedTopics.add(topic)
                            messagePanel.removeAll()
                            for (msg in messages) {
                                messagePanel.add(
                                    messageItem.getContent(msg, content.width)
                                )
                            }

                            topicPanel.preferredSize = Dimension(content.width, 50 + 100 * messages.size)
                            topicPanel.maximumSize = Dimension(Int.MAX_VALUE, 50 + 100 * messages.size)
                            topicPanel.minimumSize = Dimension(content.width, 50 + 100 * messages.size)
                        } else {
                            displayedTopics.remove(topic)
                            topicPanel.preferredSize = Dimension(content.width, 50)
                            topicPanel.maximumSize = Dimension(Int.MAX_VALUE, 50)
                            topicPanel.minimumSize = Dimension(content.width, 50)
                        }

                        content.revalidate()
                        content.repaint()
                    }
                })

                // Add messages to the topic message panel
                for (msg in messages) {
                    val messageContainer = JPanel(BorderLayout())
                    messageContainer.border = EmptyBorder(0, 0, 0, 0)

                    val messageContent = messageItem.getContent(msg, content.width)
                    messageContainer.add(messageContent, BorderLayout.CENTER)
                    messagePanel.add(messageContainer)
                }

                topicPanel.add(messagePanel, BorderLayout.CENTER)
                messageContainer.add(topicPanel)
            }
        }

        messageContainer.revalidate()
        messageContainer.repaint()
    }
}
