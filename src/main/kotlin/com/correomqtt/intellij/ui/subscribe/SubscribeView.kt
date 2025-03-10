package com.correomqtt.intellij.ui.subscribe

import com.correomqtt.intellij.core.services.subscribe.SubscribeService
import com.correomqtt.intellij.ui.common.PayloadArea
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import org.correomqtt.core.model.Qos
import org.correomqtt.di.Assisted
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Inject
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField

//TODO Extract subscribe toolbar from this class
@DefaultBean
class SubscribeView @Inject constructor(@Assisted project: Project) : JPanel(BorderLayout()) {
    private val subscribeService = service<SubscribeService>()

    init {
        val subscribeSection = getSubscribeSection()

        // Layout for main content
        val mainPanel = JPanel(GridBagLayout())
        val constraints = GridBagConstraints()

        // Add panels to mainPanel
        val messageListViewFactory = MessageListViewFactory()
        val messageListView = messageListViewFactory.create(project)

        constraints.gridx = 0
        constraints.weightx = 0.2
        constraints.weighty = 1.0
        constraints.fill = GridBagConstraints.BOTH
        constraints.insets = JBUI.insetsRight(8)
        mainPanel.add(SoyDi.inject(TopicListView::class.java), constraints)

        constraints.gridx = 1
        constraints.weightx = 0.2
        constraints.weighty = 1.0
        mainPanel.add(messageListView, constraints)

        constraints.gridx = 2
        constraints.weightx = 0.6
        constraints.weighty = 1.0
        mainPanel.add(PayloadArea().createJsonTextArea(), constraints)

        val mainScrollPane = JBScrollPane(mainPanel)
        mainScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        mainScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        mainScrollPane.border = JBUI.Borders.emptyTop(16)

        val wrapperPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(16, 0)
            add(subscribeSection, BorderLayout.WEST)
        }

        add(wrapperPanel, BorderLayout.NORTH)
        add(mainScrollPane, BorderLayout.CENTER)
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
