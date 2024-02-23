package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.GuiCore
import com.github.julienma94.intellijplugintest.core.services.activation.ApplicationActivationService
import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.core.utils.ConnectionManager
import org.correomqtt.di.Inject


class ConnectionManager : ToolWindowFactory {

    private val service = service<ConnectionManagerService>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
        val connections = service.getConnections()
        System.out.println(connections.toString())
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {
        fun getContent() = JBPanel<JBPanel<*>>().apply {

        }
    }
}
