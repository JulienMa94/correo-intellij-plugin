package com.correomqtt.intellij.ui.main

import com.correomqtt.intellij.ui.common.components.DefaultPanel
import com.correomqtt.intellij.ui.common.events.ON_CONNECTION_SELECTED_TOPIC
import com.correomqtt.intellij.ui.common.events.ConnectionSelectionListener
import com.correomqtt.intellij.ui.connection.ConnectionTree
import com.correomqtt.intellij.ui.connection.ConnectionTreeFactory
import com.correomqtt.intellij.ui.tab.TabManager
import com.correomqtt.intellij.ui.tab.TabManagerFactory
import com.intellij.openapi.project.Project
import com.intellij.ui.JBSplitter
import org.correomqtt.di.Assisted
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Inject
import java.awt.BorderLayout
import javax.swing.JPanel

@DefaultBean
class ToolWindow @Inject constructor(@Assisted project: Project): JPanel(BorderLayout()) {
    private val splitter: JBSplitter = JBSplitter(false, 0.15f)
    private val tabManager: TabManager = TabManagerFactory().create(project)
    private val connectionTree: ConnectionTree = ConnectionTreeFactory().create(project)

    init {
        val messageBus = project.messageBus.connect()
        splitter.border = null;

        messageBus.subscribe(ON_CONNECTION_SELECTED_TOPIC, object : ConnectionSelectionListener {
            override fun onConnectionSelected(name: String, id: String) {
                println("Connection selected: $name, $id")
                splitter.secondComponent = tabManager

                revalidate()
                repaint()
            }
        })
        splitter.firstComponent = connectionTree
        splitter.secondComponent = DefaultPanel("No connection selected")

        splitter.secondComponent.border = null
        add(splitter, BorderLayout.CENTER)
    }
}