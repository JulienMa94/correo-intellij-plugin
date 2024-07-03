package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
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

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private lateinit var service: ConnectionManagerService;

        fun setService(service: ConnectionManagerService) {
            this.service = service
        }

        fun getContent(): DialogPanel {
            val connections = service.getConnections()

            val panel = panel {
                group {
                    connections.forEach {
                        row {
                            text(it.name).bold()
                        }
                        row(it.hostAndPort) {
                            val connectionAction = object : DumbAwareAction("Connect to ${it.name}", "Connect to ${it.name}", AllIcons.Actions.Execute) {
                                override fun actionPerformed(e: AnActionEvent) {
                                    service.connect(it.id)
                                }
                            }
                            val disconnectAction = object : DumbAwareAction("Disconnect to ${it.name}", "Disconnect to ${it.name}", AllIcons.Actions.Cancel) {
                                override fun actionPerformed(e: AnActionEvent) {
                                    service.disconnect(it.id)
                                }
                            }
                            actionButton(connectionAction)
                            actionButton(disconnectAction)
                        }
                        separator()
                    }
                }
            }

            return panel
        }
    }
}
//
//
//row {
//    button("button") {}
//}
//
//row("tabbedPaneHeader:") {
//    tabbedPaneHeader(listOf("Tab 1", "Tab 2", "Last Tab"))
//}
//
//row("label:") {
//    label("Some label")
//}
//
//row("text:") {
//    text("text supports max line width and can contain links, try <a href='https://www.jetbrains.com'>jetbrains.com</a>.<br><icon src='AllIcons.General.Information'>&nbsp;It's possible to use line breaks and bundled icons")
//}
//
//row("link:") {
//    link("Focusable link") {}
//}
//
//row("browserLink:") {
//    browserLink("jetbrains.com", "https://www.jetbrains.com")
//}
//
//row("icon:") {
//    icon(AllIcons.Actions.QuickfixOffBulb)
//}
//
//row("contextHelp:") {
//    contextHelp("contextHelp description", "contextHelp title")
//}
//
//row("textField:") {
//    textField()
//}
//
//row("passwordField:") {
//    passwordField().applyToComponent { text = "password" }
//}
//
//row("textFieldWithBrowseButton:") {
//    textFieldWithBrowseButton()
//}
//
//row("expandableTextField:") {
//    expandableTextField()
//}
//
//row("intTextField(0..100):") {
//    intTextField(0..100)
//}
//
//row("spinner(0..100):") {
//    spinner(0..100)
//}
//
//row("spinner(0.0..100.0, 0.01):") {
//    spinner(0.0..100.0, 0.01)
//}
//
//row("comboBox:") {
//    comboBox(listOf("Item 1", "Item 2"))
//}