package com.github.julienma94.intellijplugintest.ui.main

import com.github.julienma94.intellijplugintest.CorreoPlugin
import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.github.julienma94.intellijplugintest.ui.connection.ConnectionManager
import com.github.julienma94.intellijplugintest.ui.publish.PublishView
import com.github.julienma94.intellijplugintest.ui.subscribe.SubscribeView
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBSplitter
import com.intellij.ui.content.ContentFactory
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import javax.swing.*

class MainView : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: com.intellij.openapi.wm.ToolWindow) {
        val myToolWindow = ToolWindow()

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class ToolWindow() {
        private val content: JPanel = JPanel(BorderLayout())
        private val splitter: JBSplitter = JBSplitter(false, 0.2f)
        private val tabbedPane: JTabbedPane = JTabbedPane()
        private val connectionManager: ConnectionManager = ConnectionManager()
        private var subscribeView: SubscribeView
        private val publishView: PublishView = PublishView()

        init {
            SoyDi.inject(SubscribeView::class.java).let {
                subscribeView = it
            }
            val tree = connectionManager.initializeConnectionTree(::addConnectionTab)
            splitter.firstComponent = JScrollPane(tree)
            splitter.secondComponent = JScrollPane(tabbedPane)
            content.add(splitter, BorderLayout.CENTER)
        }

        private fun addConnectionTab(title: String) {
            // Check if tab with the same title already exists
            for (i in 0 until tabbedPane.tabCount) {
                if (tabbedPane.getTitleAt(i) == title) {
                    tabbedPane.selectedIndex = i
                    return
                }
            }

            // Create tab content
            val tabContent = JPanel(BorderLayout())
            tabContent.add(getTabContent())

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

        private fun getTabContent(): JBSplitter {
            val splitter: JBSplitter = JBSplitter(false, 0.5f)

            splitter.firstComponent = publishView.getPublishContent()
            splitter.secondComponent = subscribeView.getSubscribeContent()

            return splitter
        }

        fun getContent(): JPanel {
            return content
        }
    }
}
