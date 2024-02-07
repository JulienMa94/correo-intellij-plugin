package com.github.julienma94.intellijplugintest.ui.toolWindow

import com.github.julienma94.intellijplugintest.action.ConnectAction
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.julienma94.intellijplugintest.services.MyProjectService
import com.github.julienma94.intellijplugintest.ui.mainWindow.MainEditor
import com.github.julienma94.intellijplugintest.ui.mainWindow.MainWindow
import com.github.julienma94.intellijplugintest.ui.menu.SettingsMenu
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.WindowManager
import java.awt.event.ActionListener
import javax.swing.JButton

class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = SimpleToolWindowPanel(true)
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(project), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()
        private val services = toolWindow.project.service<MainEditor>()


        fun getContent(project: Project) = JBPanel<JBPanel<*>>().apply {
            val connect = ConnectAction()
            val connectButton = JButton("Connect").apply {
                addActionListener {
                    services.component.components.forEach {
                        println(it)
                    }
                    ContentFactory.getInstance().createContent(SettingsMenu(), null, false)
                }
            }

            val settingsButton = JButton("Settings").apply {
                addActionListener {
                    ContentFactory.getInstance().createContent(SettingsMenu(), null, false)
                }

            }

            add(connectButton)
            add(settingsButton)

        }
    }
}
