package com.correomqtt.plugin.ui.main

import com.correomqtt.plugin.ui.common.components.DefaultPanel
import com.correomqtt.plugin.ui.common.events.ON_CONNECTION_SELECTED_TOPIC
import com.correomqtt.plugin.ui.common.events.ConnectionSelectionListener
import com.correomqtt.plugin.ui.connection.ConnectionTree
import com.correomqtt.plugin.ui.connection.ConnectionTreeFactory
import com.correomqtt.plugin.ui.tab.TabManager
import com.correomqtt.plugin.ui.tab.TabManagerFactory
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
    private val tabManager: TabManager = com.correomqtt.plugin.ui.tab.TabManagerFactory().create(project)
    private val connectionTree: ConnectionTree = com.correomqtt.plugin.ui.connection.ConnectionTreeFactory().create(project)

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