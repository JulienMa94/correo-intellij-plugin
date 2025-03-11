package com.correomqtt.intellij.ui.publish

import com.correomqtt.intellij.core.services.publish.PublishService
import com.correomqtt.intellij.ui.common.components.PayloadArea
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import org.correomqtt.core.model.Qos
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

//TODO Retained flag
class PublishView {
    private val publishService = service<PublishService>()
    private val content = JPanel(BorderLayout())

    fun getPublishContent(): JPanel {
        content.layout = BorderLayout()

        val payloadArea = PayloadArea().createJsonTextArea()
        val rowScrollPane = JScrollPane(payloadArea)
        rowScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.preferredSize = Dimension(content.width, content.height)
        rowScrollPane.border = EmptyBorder(16, 0, 16, 0)

        val topicTextField = JTextField()
        topicTextField.preferredSize = Dimension(350, topicTextField.preferredSize.height) // Set fixed width


        val comboBox = JComboBox(arrayOf(Qos.AT_MOST_ONCE, Qos.AT_LEAST_ONCE, Qos.EXACTLY_ONCE))
        comboBox.preferredSize = Dimension(20, comboBox.preferredSize.height) // Set fixed width

        val publishAction = object : DumbAwareAction(
            "Publishing to ${topicTextField.text}",
            "Subscribing action",
            AllIcons.Actions.Execute
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                publishService.publish(topicTextField.text, payloadArea.text, comboBox.selectedItem as Qos, false)
            }
        }

        val publishButton = JButton("Publish")
        publishButton.addActionListener {
            publishAction.actionPerformed(
                AnActionEvent.createFromAnAction(
                    publishAction,
                    null,
                    "",
                    DataContext.EMPTY_CONTEXT
                )
            )
        }


        // Set the layout and constraints
        val gridBagLayout = GridBagLayout()
        val constraints = GridBagConstraints()
        val panel = JPanel(gridBagLayout)
        // Add the JTextField to the panel
        constraints.weightx = 0.7
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        panel.add(topicTextField, constraints)


        constraints.gridx = 1
        panel.add(comboBox, constraints)

        constraints.gridx = 2
        panel.add(publishButton, constraints)

        content.add(panel, BorderLayout.NORTH)
        content.add(rowScrollPane, BorderLayout.CENTER)

        return content;

    }
}