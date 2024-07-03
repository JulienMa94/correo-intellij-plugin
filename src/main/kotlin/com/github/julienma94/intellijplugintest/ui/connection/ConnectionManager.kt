package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBSplitter
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel


class ConnectionManager : ToolWindowFactory {


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(project), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow() {

        private val service = service<ConnectionManagerService>()
        private val content: JPanel = JPanel(BorderLayout())
        private val tree: JTree = JTree()
        private val splitter: JBSplitter = JBSplitter(false, 0.3f)
        private val tabbedPane: JTabbedPane = JTabbedPane()

        init {
            initializeConnectionTree()

            // TabbedPane setup
            splitter.secondComponent = tabbedPane
            content.add(splitter, BorderLayout.CENTER)
        }

        private fun initializeConnectionTree() {
            val connections = service.getConnections();

            // Tree setup
            val root = DefaultMutableTreeNode("MQTT")
            connections.forEach {
                val connection = DefaultMutableTreeNode(it.name)
                root.add(connection)
            }

            tree.model = DefaultTreeModel(root)
            splitter.firstComponent = JScrollPane(tree)

            // Add mouse listener to handle double-clicks
            tree.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.clickCount == 2) {
                        val selectedNode = tree.lastSelectedPathComponent as? DefaultMutableTreeNode
                        if (selectedNode != null && selectedNode.isLeaf) {
                            val tabTitle = selectedNode.userObject.toString()
                            addTab(tabTitle)
                        }
                    }
                }
            })
        }

        private fun addTab(title: String) {
            // Check if tab with the same title already exists
            for (i in 0 until tabbedPane.tabCount) {
                if (tabbedPane.getTitleAt(i) == title) {
                    tabbedPane.selectedIndex = i
                    return
                }
            }

            // Create tab content
            val tabContent = JPanel(BorderLayout())
            tabContent.add(JLabel("Content for $title"), BorderLayout.CENTER)

            // Add tab with custom tab component
            tabbedPane.addTab(title, tabContent)
            val tabIndex = tabbedPane.indexOfComponent(tabContent)
            tabbedPane.setTabComponentAt(tabIndex, createTabComponent(title))

            // Select the new tab
            tabbedPane.selectedIndex = tabIndex
        }

        private fun createTabComponent(title: String): JPanel {
            val tabComponent = JPanel(BorderLayout())
            tabComponent.isOpaque = false

            val titleLabel = JLabel(title)
            val closeButton = JButton("x")

            closeButton.border = BorderFactory.createEmptyBorder()
            closeButton.isContentAreaFilled = false
            closeButton.addActionListener {
                val tabIndex = tabbedPane.indexOfTabComponent(tabComponent)
                if (tabIndex != -1) {
                    tabbedPane.remove(tabIndex)
                }
            }

            tabComponent.add(titleLabel, BorderLayout.CENTER)
            tabComponent.add(closeButton, BorderLayout.EAST)
            return tabComponent
        }

        fun getContent(): JPanel {
            return content
        }
    }
}
