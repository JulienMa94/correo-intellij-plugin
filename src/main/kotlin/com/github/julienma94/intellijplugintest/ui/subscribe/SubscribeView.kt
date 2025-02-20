package com.github.julienma94.intellijplugintest.ui.subscribe

import com.github.julienma94.intellijplugintest.core.services.subscribe.SubscribeService
import com.github.julienma94.intellijplugintest.ui.common.PayloadArea
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBScrollPane
import org.correomqtt.core.model.Qos
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.SoyDi
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

//TODO Extract subscribe toolbar from this class
@DefaultBean
class SubscribeView {
    private val subscribeService = service<SubscribeService>()
    private val content: JPanel = JPanel(BorderLayout())

    fun getSubscribeContent(project: Project): JPanel {
        content.layout = BorderLayout()
        val subscribeSection = getSubscribeSection()

        // Layout for main content
        val mainPanel = JPanel(GridBagLayout())
        val constraints = GridBagConstraints()

        // Add panels to mainPanel
        val messageListView = SoyDi.inject(MessageListView::class.java)
        messageListView.addProject(project)


        constraints.gridx = 0
        constraints.weightx = 0.1
        constraints.weighty = 1.0
        constraints.fill = GridBagConstraints.BOTH
        constraints.insets = Insets(0, 0, 0, 8)
        mainPanel.add(SoyDi.inject(TopicListView::class.java), constraints)

        constraints.gridx = 1
        constraints.weightx = 0.1
        constraints.weighty = 1.0
        mainPanel.add(messageListView, constraints)

        constraints.gridx = 2
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        mainPanel.add(PayloadArea().createJsonTextArea(), constraints)

        val mainScrollPane = JBScrollPane(mainPanel)
        mainScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        mainScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        mainScrollPane.border = EmptyBorder(16, 0, 0, 0)

        val wrapperPanel = JPanel(BorderLayout()).apply {
            border = EmptyBorder(16, 0, 16, 0)
            add(subscribeSection, BorderLayout.WEST)
        }

        content.add(wrapperPanel, BorderLayout.NORTH)
        content.add(mainScrollPane, BorderLayout.CENTER)
        return content
    }

    private fun getSubscribeSection(): JPanel {
        val subscribeSection = JPanel(BorderLayout())

        // Subscribe Section
        val textField = JTextField()
        textField.preferredSize = Dimension(400, textField.preferredSize.height) // Set fixed width
        val comboBox = ComboBox(arrayOf(Qos.AT_MOST_ONCE, Qos.AT_LEAST_ONCE, Qos.EXACTLY_ONCE))
        comboBox.preferredSize = Dimension(100, comboBox.preferredSize.height) // Set fixed width
        comboBox.maximumSize = Dimension(100, comboBox.preferredSize.height) // Set fixed width

        val subscribeAction = object : DumbAwareAction(
            "Subscribing to ${textField.text}",
            "Subscribing action",
            AllIcons.Actions.Execute
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                val topic = textField.text
                subscribeService.subscribe(topic, comboBox.selectedItem as Qos)
            }
        }
        val subscribeButton = JButton("Subscribe")
        subscribeButton.preferredSize = Dimension(100, subscribeButton.preferredSize.height) // Set fixed width
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
        constraints.gridx = 0
        panel.add(textField, constraints)
        constraints.gridx = 1
        panel.add(comboBox, constraints)
        constraints.gridx = 2
        panel.add(subscribeButton, constraints)

        subscribeSection.add(panel, BorderLayout.NORTH)

        return subscribeSection
    }
}
