package com.github.julienma94.intellijplugintest.ui.subscribe

import com.github.julienma94.intellijplugintest.core.services.subscribe.SubscribeService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBScrollPane
import org.correomqtt.core.model.Qos
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.core.pubsub.SubscribeEvent
import org.correomqtt.core.pubsub.UnsubscribeEvent
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Observes
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.border.EmptyBorder
import kotlin.math.log

@DefaultBean
class SubscribeView {
    private val subscribeService = service<SubscribeService>()
    private val content: JPanel = JPanel(BorderLayout())
    private val messageContainer: JPanel = JPanel()
    private val topicsPanel: JPanel = JPanel()
    private val messagesPanel: JPanel = JPanel()
    private val payloadPanel: JPanel = JPanel()

    private val messages: MutableMap<String, MutableList<IncomingMessageEvent>> = mutableMapOf()
    private val displayedTopics = mutableSetOf<String>();

    init {
        messageContainer.layout = BoxLayout(messageContainer, BoxLayout.Y_AXIS)
        messageContainer.border = EmptyBorder(0, 0, 0, 0)
        topicsPanel.layout = BoxLayout(topicsPanel, BoxLayout.Y_AXIS)
        messagesPanel.layout = BoxLayout(messagesPanel, BoxLayout.Y_AXIS)
        payloadPanel.layout = BoxLayout(payloadPanel, BoxLayout.Y_AXIS)
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

        val subscribeSection = getSubscribeSection()

        // Layout for main content
        val mainPanel = JPanel(GridLayout(1, 3))

        topicsPanel.border = BorderFactory.createTitledBorder("Topics")
        messagesPanel.border = BorderFactory.createTitledBorder("Messages")
        payloadPanel.border = BorderFactory.createTitledBorder("Payload")

        // Add panels to mainPanel
        mainPanel.add(topicsPanel)
        mainPanel.add(messagesPanel)
        mainPanel.add(payloadPanel)

        val rowScrollPane = JBScrollPane(mainPanel)
        rowScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        rowScrollPane.border = null

        content.add(subscribeSection, BorderLayout.NORTH)
        content.add(rowScrollPane, BorderLayout.CENTER)
        return content
    }

    private fun getSubscribeSection(): JPanel {
        val subscribeSection = JPanel(BorderLayout())

        // Subscribe Section
        val textField = JTextField()
        val comboBox = ComboBox(arrayOf(Qos.AT_MOST_ONCE, Qos.AT_LEAST_ONCE, Qos.EXACTLY_ONCE))
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
        // Populate topicsPanel with topic buttons
        topicsPanel.removeAll()


        messages.forEach { (topic, _) ->
            val topicButton = JButton(topic)
            topicButton.addActionListener {
                displayMessages(topic)
            }
            topicsPanel.add(topicButton)
        }

        topicsPanel.revalidate()
        topicsPanel.repaint()
    }

    private fun displayMessages(topic: String) {

        messagesPanel.removeAll()
        messages[topic]?.forEach { message ->

            val messageButton = JButton("Message at ${message.messageDTO.dateTime}")
            messageButton.addActionListener {
                displayPayload(message)
            }
            messagesPanel.add(messageButton)
        }

        messagesPanel.revalidate()
        messagesPanel.repaint()
    }


    private fun displayPayload(message: IncomingMessageEvent) {
        payloadPanel.removeAll()

        val payloadLabel = JLabel("Payload: ${message.messageDTO.payload}")
        payloadPanel.add(payloadLabel)
        payloadPanel.revalidate()
        payloadPanel.repaint()

    }
}
