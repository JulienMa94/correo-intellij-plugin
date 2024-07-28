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
    private val rowContainer: JPanel = JPanel()

    private val messages: MutableMap<String, MutableList<IncomingMessageEvent>> = mutableMapOf()
    private val displayedTopics = mutableSetOf<String>();

    init {
        rowContainer.layout = BoxLayout(rowContainer, BoxLayout.Y_AXIS)
        rowContainer.border = EmptyBorder(16, 8, 16, 8)
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
        currentMessages.add(message)
        messages[topic] = currentMessages
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

        val rowScrollPane = JScrollPane(rowContainer)
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
        SwingUtilities.invokeLater {
            rowContainer.removeAll()
            val colorsScheme = EditorColorsManager.getInstance().globalScheme

            if (messages.isEmpty()) {
                val defaultPanel = DefaultPanel().getContent("Subscribe to a topic to see received messages")
                rowContainer.add(defaultPanel)
            } else {
                for ((topic, messages) in messages) {
                    val topicPanel = JPanel(BorderLayout())
                    topicPanel.border = null

                    val headerPanel = JPanel(BorderLayout())
                    headerPanel.border = EmptyBorder(5, 5, 5, 5)
                    val topicLabel = JLabel(topic)
                    topicLabel.font = topicLabel.font.deriveFont(Font.BOLD)

                    val countLabel = JLabel(messages.size.toString())
                    countLabel.border = EmptyBorder(5, 5, 5, 5)

                    val icon =
                        if (displayedTopics.contains(topic)) AllIcons.General.ArrowDown else AllIcons.General.ArrowRight
                    val iconLabel = JLabel(icon)
                    headerPanel.add(topicLabel, BorderLayout.WEST)
                    headerPanel.add(countLabel, BorderLayout.CENTER)
                    headerPanel.add(iconLabel, BorderLayout.EAST)

                    headerPanel.border = BorderFactory.createMatteBorder(0, 0, 1, 0, colorsScheme.defaultBackground)
                    headerPanel.preferredSize = Dimension(rowContainer.width, 50)
                    headerPanel.maximumSize = Dimension(Int.MAX_VALUE, 50)
                    headerPanel.minimumSize = Dimension(rowContainer.width, 50)

                    topicPanel.add(headerPanel, BorderLayout.NORTH)

                    val messagePanel = JPanel()
                    val messageRoot = JScrollPane(messagePanel)
                    messageRoot.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                    messageRoot.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                    messageRoot.border = null

                    messagePanel.preferredSize = Dimension(messageRoot.width, 200)
                    messagePanel.maximumSize = Dimension(messageRoot.width, 200)

                    messagePanel.layout = BoxLayout(messagePanel, BoxLayout.Y_AXIS)
                    messagePanel.isVisible = displayedTopics.contains(topic)

                    topicPanel.preferredSize = if (messagePanel.isVisible) Dimension(
                        rowContainer.width,
                        50 + 50 * messages.size
                    ) else Dimension(rowContainer.width, 50)
                    topicPanel.maximumSize = if (messagePanel.isVisible) Dimension(
                        Int.MAX_VALUE,
                        50 + 50 * messages.size
                    ) else Dimension(Int.MAX_VALUE, 50)
                    topicPanel.minimumSize = if (messagePanel.isVisible) Dimension(
                        rowContainer.width,
                        50 + 50 * messages.size
                    ) else Dimension(rowContainer.width, 50)

                    val messageItem = MessageItem();

                    headerPanel.addMouseListener(object : MouseAdapter() {
                        override fun mouseClicked(e: MouseEvent?) {
                            messagePanel.isVisible = !messagePanel.isVisible

                            if (messagePanel.isVisible) {
                                displayedTopics.add(topic)
                                messagePanel.removeAll()
                                for (msg in messages) {
                                    messagePanel.add(messageItem.getContent(msg, rowContainer.width))
                                }

                                topicPanel.preferredSize = Dimension(rowContainer.width, 50 + 50 * messages.size)
                                topicPanel.maximumSize = Dimension(Int.MAX_VALUE, 50 + 50 * messages.size)
                                topicPanel.minimumSize = Dimension(rowContainer.width, 50 + 50 * messages.size)
                            } else {
                                displayedTopics.remove(topic)
                                topicPanel.preferredSize = Dimension(rowContainer.width, 50)
                                topicPanel.maximumSize = Dimension(Int.MAX_VALUE, 50)
                                topicPanel.minimumSize = Dimension(rowContainer.width, 50)
                            }

                            iconLabel.icon =
                                if (messagePanel.isVisible) AllIcons.General.ArrowDown else AllIcons.General.ArrowRight
                            rowContainer.revalidate()
                            rowContainer.repaint()
                        }
                    })

                    for (msg in messages) {
                        messagePanel.add(messageItem.getContent(msg, rowContainer.width))
                    }

                    topicPanel.add(messageRoot, BorderLayout.CENTER)
                    rowContainer.add(topicPanel)
                }
            }
            rowContainer.revalidate()
            rowContainer.repaint()
        }
    }
}