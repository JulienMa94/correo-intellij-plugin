package com.github.julienma94.intellijplugintest.ui.tab

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.github.julienma94.intellijplugintest.ui.publish.PublishView
import com.github.julienma94.intellijplugintest.ui.subscribe.SubscribeView
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBTabbedPane
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

class TabManager() {
    private val tabbedPane = JBTabbedPane()
    private val connectionService = service<ConnectionManagerService>()

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
        val tabComponent = JPanel()
        tabComponent.layout = BoxLayout(tabComponent, BoxLayout.X_AXIS)
        tabComponent.isOpaque = false

        val titleLabel = JLabel(title)
        titleLabel.horizontalAlignment = SwingConstants.CENTER

        val closeButton = JButton()
        closeButton.border = BorderFactory.createEmptyBorder()
        closeButton.preferredSize = Dimension(16, 16)
        closeButton.isContentAreaFilled = false
        closeButton.addActionListener {
            val tabIndex = tabbedPane.indexOfTabComponent(tabComponent)
            if (tabIndex != -1) {
                tabbedPane.remove(tabIndex)
                connectionService.disconnect(tabIndex)
            }
        }

        // Set hover effect
        val closeButtonHoverIcon = AllIcons.Actions.CloseHovered
        val closeButtonDefaultIcon = AllIcons.Actions.CloseDarkGrey

        closeButton.icon = closeButtonDefaultIcon

        closeButton.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseEntered(e: java.awt.event.MouseEvent?) {
                closeButton.icon = closeButtonHoverIcon
            }

            override fun mouseExited(e: java.awt.event.MouseEvent?) {
                closeButton.icon = closeButtonDefaultIcon
            }
        })

        // Title panel to help with centering
        val titlePanel = JPanel(BorderLayout())
        titlePanel.isOpaque = false
        titlePanel.add(titleLabel, BorderLayout.CENTER)

        // Add components to the main tab component
        tabComponent.add(titlePanel)
        tabComponent.add(Box.createHorizontalStrut(8)) // Adding padding between title and close button
        tabComponent.add(closeButton)

        return tabComponent
    }


    private fun getTabContent(): JBSplitter {
        val splitter = JBSplitter(false, 0.5f)
        val subscribeView = SoyDi.inject(SubscribeView::class.java)
        val publishView = PublishView()

        splitter.firstComponent = subscribeView.getSubscribeContent()
        splitter.secondComponent = publishView.getPublishContent()

        return splitter
    }
}
