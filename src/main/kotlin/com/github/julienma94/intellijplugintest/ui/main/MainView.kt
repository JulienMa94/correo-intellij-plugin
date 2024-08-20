package com.github.julienma94.intellijplugintest.ui.main

import com.github.julienma94.intellijplugintest.core.services.secruity.KeyringManagerService
import com.github.julienma94.intellijplugintest.ui.common.DefaultPanel
import com.github.julienma94.intellijplugintest.ui.connection.CONNECTION_SELECTED_TOPIC
import com.github.julienma94.intellijplugintest.ui.connection.ConnectionSelectionListener
import com.github.julienma94.intellijplugintest.ui.connection.ConnectionTree
import com.github.julienma94.intellijplugintest.ui.tab.TabManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBSplitter
import com.intellij.ui.content.ContentFactory
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import javax.swing.JPanel

class MainView : ToolWindowFactory {

    private val keyringManagerService = service<KeyringManagerService>()

    override fun createToolWindowContent(project: Project, toolWindow: com.intellij.openapi.wm.ToolWindow) {
        val myToolWindow = ToolWindow(project)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class ToolWindow(project: Project) {
        private val content: JPanel = JPanel(BorderLayout())
        private val splitter: JBSplitter = JBSplitter(false, 0.12f)
        private val connectionTree: ConnectionTree = SoyDi.inject(ConnectionTree::class.java)
        private val tabManager: TabManager = TabManager()

        init {
            connectionTree.addProject(project)
            val connection = project.messageBus.connect()
            splitter.border = null;

            connection.subscribe(CONNECTION_SELECTED_TOPIC, object : ConnectionSelectionListener {
                override fun onConnectionSelected(name: String, id: String) {
                    println("Connection selected: $name, $id")
                    tabManager.createTab(id, name)
                    splitter.secondComponent = tabManager.getTabbedPane()

                    content.revalidate()
                    content.repaint()
                }
            })
            splitter.firstComponent = connectionTree
            splitter.secondComponent = DefaultPanel().getContent("No connection selected")

            splitter.secondComponent.border = null
            content.add(splitter, BorderLayout.CENTER)
        }

        fun getContent(): JPanel {
            return content
        }
    }
}
