package com.github.julienma94.intellijplugintest.ui.main

import com.github.julienma94.intellijplugintest.ui.connection.ConnectionTree
import com.github.julienma94.intellijplugintest.ui.tab.TabManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBSplitter
import com.intellij.ui.content.ContentFactory
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JScrollPane

class MainView : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: com.intellij.openapi.wm.ToolWindow) {
        val myToolWindow = ToolWindow()

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class ToolWindow() {
        private val content: JPanel = JPanel(BorderLayout())
        private val splitter: JBSplitter = JBSplitter(false, 0.12f)
        private val connectionManager: ConnectionTree = SoyDi.inject(ConnectionTree::class.java)
        private val tabManager: TabManager = TabManager()


        init {
            val tree = connectionManager.initializeConnectionTree(::onDoubleClick)
            val tabbedPane = tabManager.getTabbedPane();
            splitter.firstComponent = JScrollPane(tree)
            splitter.secondComponent = JScrollPane(tabbedPane)
            val colorsScheme = EditorColorsManager.getInstance().globalScheme
            val customBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, colorsScheme.defaultBackground)
            splitter.firstComponent.border = customBorder
            splitter.secondComponent.border = null

            content.add(splitter, BorderLayout.CENTER)
        }

        private fun onDoubleClick(tabTitle: String, connectionId: String) {
            tabManager.createTab(connectionId, tabTitle)
        }

        fun getContent(): JPanel {
            return content
        }
    }
}
