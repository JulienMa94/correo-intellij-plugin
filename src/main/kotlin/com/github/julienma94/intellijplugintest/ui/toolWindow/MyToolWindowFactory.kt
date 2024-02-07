package com.github.julienma94.intellijplugintest.ui.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.julienma94.intellijplugintest.services.MyProjectService
import com.github.julienma94.intellijplugintest.ui.mainWindow.MainWindow
import com.github.julienma94.intellijplugintest.ui.menu.SettingsMenu
import javax.swing.JButton

class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()


        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val connectButton = JButton("Connect").apply {
                addActionListener {
                    ContentFactory.getInstance().createContent(MainWindow(), null, false)
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
