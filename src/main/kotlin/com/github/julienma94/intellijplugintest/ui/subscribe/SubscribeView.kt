package com.github.julienma94.intellijplugintest.ui.subscribe;

import com.github.julienma94.intellijplugintest.core.services.subscribe.SubscribeService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import org.correomqtt.core.model.Qos
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.core.pubsub.SubscribeEvent
import org.correomqtt.core.pubsub.UnsubscribeEvent
import org.correomqtt.di.Observes
import org.correomqtt.di.SingletonBean
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import javax.swing.border.LineBorder

@SingletonBean
class SubscribeView {
    private val subscribeService = service<SubscribeService>()
    private val content: JPanel = JPanel(BorderLayout())
    private var rowContainer: JPanel = JPanel()

    private val messages: MutableMap<String, List<IncomingMessageEvent>> = mutableMapOf()

    init {
        rowContainer.layout = BoxLayout(rowContainer, BoxLayout.Y_AXIS);
    }

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Received incoming message event for topic ${event.messageDTO.topic}")
        addMessageToMap(event);
        addRow(event)
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
        rowScrollPane.border = null

        content.add(getSubscribeSection(), BorderLayout.NORTH)
        content.add(rowScrollPane, BorderLayout.CENTER)
        return content;
    }

    private fun getSubscribeSection(): DialogPanel {
        return panel {
            val textField = JTextField()

            val comboBox = JComboBox(arrayOf(Qos.AT_MOST_ONCE, Qos.AT_LEAST_ONCE, Qos.EXACTLY_ONCE))
            comboBox.preferredSize = Dimension(20, comboBox.preferredSize.height) // Set fixed width

            val subscribeAction = object : DumbAwareAction(
                "Subscribing to ${textField.text}",
                "Subscribing action",
                AllIcons.Actions.Execute
            ) {
                override fun actionPerformed(e: AnActionEvent) {
                    subscribeService.subscribe(textField.text, comboBox.selectedItem as Qos)
                }
            }
            val subscribeButton = JButton("Subscribe")
            subscribeButton.addActionListener {
                subscribeAction.actionPerformed(AnActionEvent.createFromAnAction(subscribeAction, null, "", DataContext.EMPTY_CONTEXT))
            }

            // Set the layout and constraints
            val gridBagLayout = GridBagLayout()
            val constraints = GridBagConstraints()
            val panel = JPanel(gridBagLayout)
            // Add the JTextField to the panel
            constraints.weightx = 0.7
            constraints.fill = GridBagConstraints.HORIZONTAL
            constraints.gridx = 0
            constraints.gridy = 0
            panel.add(textField, constraints)

            // Add the JComboBox to the panel
            constraints.weightx = 0.25
            constraints.gridx = 1
            panel.add(comboBox, constraints)

            // Add the publish button to the panel
            constraints.weightx = 0.05
            constraints.gridx = 2
            panel.add(subscribeButton, constraints)
            row {
               cell(panel).align(AlignX.FILL)
            }
        }
    }

    private fun addRow(message: IncomingMessageEvent) {
        SwingUtilities.invokeLater {
            val colorsScheme = EditorColorsManager.getInstance().globalScheme

            val row = JPanel(BorderLayout())
            row.border = null;

            row.add(getRowContent(message), BorderLayout.WEST)

            // Add a line border at the bottom of the row, except for the last row
            if (rowContainer.components.isNotEmpty()) {
                val lastRow = rowContainer.components.last() as JPanel
                lastRow.border = BorderFactory.createMatteBorder(0, 0, 1, 0, colorsScheme.defaultBackground)
            }
            row.preferredSize = Dimension(rowContainer.width, 50)
            row.border = LineBorder(colorsScheme.defaultBackground)
            row.maximumSize = Dimension(Int.MAX_VALUE, 50)
            row.minimumSize = Dimension(rowContainer.width, 50)

            rowContainer.add(row)

            rowContainer.revalidate()
            rowContainer.repaint()

            // Ensure the new row is visible
            val scrollPane = rowContainer.parent.parent as JScrollPane
            scrollPane.verticalScrollBar.value = scrollPane.verticalScrollBar.maximum
        }
    }

    private fun getRowContent(message: IncomingMessageEvent): DialogPanel {
        return panel() {
            row {
                panel {
                    row {
                        label(message.messageDTO.topic)
                        text(message.messageDTO.qos.toString(), 50)
                    }
                }
                text(message.messageDTO.payload, 50)
            }
        }
    }
}
