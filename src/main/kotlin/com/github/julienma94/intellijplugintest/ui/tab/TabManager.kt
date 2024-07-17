package com.github.julienma94.intellijplugintest.ui.tab

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.github.julienma94.intellijplugintest.ui.publish.PublishView
import com.github.julienma94.intellijplugintest.ui.subscribe.SubscribeView
import com.intellij.openapi.components.service
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBTabbedPane
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class TabManager() {
    private var subscribeView: SubscribeView
    private val publishView: PublishView = PublishView()
    private val tabbedPane = JBTabbedPane()
    private val connectionService = service<ConnectionManagerService>()

    init {
       SoyDi.inject(SubscribeView::class.java).let {
                subscribeView = it
       }
    }

    fun getTabbedPane(): JBTabbedPane {
        return tabbedPane
    }

    fun createTab(connectionId: String, title: String) {
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
        connectionService.addConnectionId(tabIndex, connectionId)

        // Select the new tab
        tabbedPane.selectedIndex = tabIndex
        connectionService.setActiveConnectionId(connectionId)
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
                connectionService.disconnect(tabIndex)
            }
        }

        tabComponent.add(titleLabel, BorderLayout.CENTER)
        tabComponent.add(closeButton, BorderLayout.EAST)
        return tabComponent
    }


    private fun getTabContent(): JBSplitter {
        val splitter: JBSplitter = JBSplitter(false, 0.5f)

        splitter.firstComponent = subscribeView.getSubscribeContent()
        splitter.secondComponent = publishView.getPublishContent()

        return splitter
    }
}

/*class DeleteTabAction(private val service: ConnectionConfigDTO, private val project: Project) :
    AnAction("Delete Tab", "Delete the currently selected tab", AllIcons.Actions.Cancel) {
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

}*/

/*
private fun addConnectionTab(title: String, connection: ConnectionConfigDTO) {
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




*/
