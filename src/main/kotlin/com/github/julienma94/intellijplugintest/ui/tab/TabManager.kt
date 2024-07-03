package com.github.julienma94.intellijplugintest.ui.tab

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.content.ContentFactory
import org.correomqtt.core.model.ConnectionConfigDTO
import javax.swing.JLabel

class CreateTabAction(private val service: ConnectionConfigDTO, private val project: Project) : AnAction("Create JPTabbedPane", "Create JPTabbedPane", AllIcons.Actions.Execute) {
    private val services = service<ConnectionManagerService>()

    override fun actionPerformed(e: AnActionEvent) {
        services.connect(service.id)
        val toolWindowManager = ToolWindowManager.getInstance(project)

        val toolWindow = toolWindowManager.getToolWindow("Correo")

        val tabs = toolWindow?.contentManager?.contents
        var tabExist = false
        if (tabs != null) {
            for (tab in tabs) {
                println(tab.tabName)
                if (tab?.tabName.equals(service.id)) {
                    println("Tab already exists")
                    tabExist = true
                }
            }
        }
        if (!tabExist) {
            val tabbedPane = JBTabbedPane()

            // Add content to the tab
            println("Creating JPTabbedPane")
            val tabName = "Tab " + (tabbedPane.tabCount + 1)
            tabbedPane.addTab(tabName, JLabel("Hello World, $tabName"))

            val contentFactory = ContentFactory.getInstance()
            val content = contentFactory.createContent(tabbedPane, service.id, false)

            toolWindow?.contentManager?.addContent(content)
            toolWindow?.contentManager?.setSelectedContent(content)
        }
    }
}

class DeleteTabAction(private val service: ConnectionConfigDTO, private val project: Project) : AnAction("Delete Tab", "Delete the currently selected tab", AllIcons.Actions.Cancel) {
    private val services = service<ConnectionManagerService>()

    override fun actionPerformed(e: AnActionEvent) {
        services.disconnect(service.id)

        val toolWindowManager = ToolWindowManager.getInstance(project)

        val toolWindow = toolWindowManager.getToolWindow("Correo")


        val tabs = toolWindow?.contentManager?.contents
        if (tabs != null) {
            for (tab in tabs) {
                if (tab?.tabName.equals(service.id)) {
                    println("Deleting Tab ${service.id}")
                    toolWindow.contentManager.removeContent(tab, true)
                }
            }
        }

    }

}