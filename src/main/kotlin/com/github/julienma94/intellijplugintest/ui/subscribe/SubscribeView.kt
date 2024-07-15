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
import org.correomqtt.di.Observes
import org.correomqtt.di.SingletonBean
import org.jdesktop.swingx.JXCollapsiblePane
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.JToggleButton

@SingletonBean
class SubscribeView {
    private val service = service<ConnectionManagerService>()

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Got message ${event.messageDTO.payload}")
    }

    fun onSubscribeToTopic(@Observes event: SubscribeEvent) {
        println("Received subscription event for topic ${event.subscriptionDTO.topic}")
    }

    public fun getSubscribeContent(): DialogPanel {
        return panel {
            group("Subscription", true) {
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
    }

    private fun createCollapsiblePane(title: String, messages: List<String>): JPanel {
        val collapsiblePane = JXCollapsiblePane()
        collapsiblePane.layout = GridLayout(0, 1)
        messages.forEach { message ->
            collapsiblePane.add(JLabel(message))
        }

        val toggleButton = JToggleButton(title)
        toggleButton.addItemListener {
            collapsiblePane.isCollapsed = !toggleButton.isSelected
        }

        val panel = JPanel(BorderLayout())
        panel.add(toggleButton, BorderLayout.NORTH)
        panel.add(collapsiblePane, BorderLayout.CENTER)

        return panel
    }
}
