package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.github.julienma94.intellijplugintest.ui.tab.CreateTabAction
import com.github.julienma94.intellijplugintest.ui.tab.DeleteTabAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.panel



class ConnectionManager : ToolWindowFactory {

    private val service = service<ConnectionManagerService>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val myToolWindow = MyToolWindow(toolWindow)
        myToolWindow.setService(service)

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(project), null, false)
        toolWindow.contentManager.addContent(content)
    }


    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private lateinit var service: ConnectionManagerService;


        fun setService(service: ConnectionManagerService) {
            this.service = service
        }



        fun getContent(project: Project): DialogPanel {
            val connections = service.getConnections()

            val panel = panel {
                group ("Connections") {
                    connections.forEach {
                        row {
                            text(it.name).bold()
                        }
                        row(it.hostAndPort) {
                            actionButton(CreateTabAction(it, project))
                            actionButton(DeleteTabAction(it, project))
                        }
                        separator()
                    }
                }
            }

            return panel
        }
    }
}
