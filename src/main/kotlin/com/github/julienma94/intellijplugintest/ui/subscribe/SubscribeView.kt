package com.github.julienma94.intellijplugintest.ui.subscribe;

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.core.pubsub.SubscribeEvent
import org.correomqtt.core.pubsub.UnsubscribeEvent
import org.correomqtt.di.Observes
import org.correomqtt.di.SingletonBean
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.LineBorder

@SingletonBean
class SubscribeView {
    private val service = service<ConnectionManagerService>()
    private val content: JPanel = JPanel(BorderLayout())
    private var rowContainer: JPanel = JPanel()

    private val messages: MutableMap<String, List<IncomingMessageEvent>> = mutableMapOf()

    init {
        rowContainer.layout = BoxLayout(rowContainer, BoxLayout.Y_AXIS);
    }

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Received incoming message event for topic ${event.messageDTO.topic}")
        addMessageToMap(event);
        addRow(event.messageDTO.payload)
    }

    private fun addMessageToMap(message: IncomingMessageEvent) {
        val topic = message.messageDTO.topic
        val currentMessages = messages.getOrDefault(topic, listOf())
        messages[topic] = currentMessages + message
    }

    fun onSubscribeToTopic(@Observes event: SubscribeEvent) {
        println("Received subscription event for topic ${event.subscriptionDTO.topic}")
    }

    fun onUnsubscribe(@Observes event: UnsubscribeEvent) {
        println("Received unsubscribe event for topic ${event.subscriptionDTO.topic}")
    }

    fun getSubscribeContent(): JPanel {
        content.layout = BorderLayout()

        val rowScrollPane = JScrollPane(rowContainer)
        rowScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        rowScrollPane.preferredSize = Dimension(content.width, content.height)


        content.add(getSubscribeSection(), BorderLayout.NORTH)
        content.add(rowScrollPane, BorderLayout.CENTER)
        return content;
    }

    private fun getSubscribeSection(): DialogPanel {
        return panel {
            row {
                val textField = JTextField()

                val subscribeAction = object : DumbAwareAction(
                    "Subscribing to ${textField.text}",
                    "Subscribing action",
                    AllIcons.Actions.Execute
                ) {
                    override fun actionPerformed(e: AnActionEvent) {
                        service.subscribe(textField.text)
                    }
                }

                cell(textField).resizableColumn().align(AlignX.FILL)
                actionButton(subscribeAction)
            }
        }
    }

    private fun addRow(message: String) {
        SwingUtilities.invokeLater {
            val row = JPanel(BorderLayout())
            row.add(JLabel(message), BorderLayout.WEST)
            row.preferredSize = Dimension(rowContainer.width, 50)
            row.border = LineBorder(Color.BLACK)
            row.maximumSize = Dimension(Int.MAX_VALUE, 50)
            row.minimumSize = Dimension(rowContainer.width, 50)

            rowContainer.add(row)
            rowContainer.add(Box.createVerticalStrut(5))

            rowContainer.revalidate()
            rowContainer.repaint()

            // Ensure the new row is visible
            val scrollPane = rowContainer.parent.parent as JScrollPane
            scrollPane.verticalScrollBar.value = scrollPane.verticalScrollBar.maximum
        }
    }
}
