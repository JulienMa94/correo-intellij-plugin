package com.correomqtt.intellij.ui.subscribe

import com.correomqtt.intellij.ui.subscribe.message.MessageListViewFactory
import com.correomqtt.intellij.ui.subscribe.payload.PayloadView
import com.correomqtt.intellij.ui.subscribe.topic.TopicListView
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import org.correomqtt.di.Assisted
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Inject
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

@DefaultBean
class SubscribeView @Inject constructor(@Assisted project: Project, topicListView: TopicListView) : JPanel(BorderLayout()) {

    init {
        val messageListView = MessageListViewFactory().create(project)
        val toolbar = Toolbar()
        val payloadArea = PayloadView(project)

        val mainPanel = JPanel(GridBagLayout())
        val mainScrollPane = JBScrollPane(mainPanel)

        val constraints = GridBagConstraints()
        constraints.gridx = 0
        constraints.weightx = 0.2
        constraints.weighty = 1.0
        constraints.fill = GridBagConstraints.BOTH
        constraints.insets = JBUI.insetsRight(8)
        mainPanel.add(topicListView, constraints)

        constraints.gridx = 1
        constraints.weightx = 0.2
        constraints.weighty = 1.0
        mainPanel.add(messageListView, constraints)

        constraints.gridx = 2
        constraints.weightx = 0.6
        constraints.weighty = 1.0
        mainPanel.add(payloadArea, constraints)

        mainScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        mainScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        mainScrollPane.border = JBUI.Borders.emptyTop(16)

        val wrapperPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(16, 0)
            add(toolbar, BorderLayout.WEST)
        }

        add(wrapperPanel, BorderLayout.NORTH)
        add(mainScrollPane, BorderLayout.CENTER)
    }
}
