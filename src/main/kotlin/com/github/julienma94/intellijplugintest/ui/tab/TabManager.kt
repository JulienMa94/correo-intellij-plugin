package com.github.julienma94.intellijplugintest.ui.tab

import com.github.julienma94.intellijplugintest.ui.publish.PublishView
import com.github.julienma94.intellijplugintest.ui.subscribe.SubscribeView
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import org.correomqtt.di.SoyDi
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class TabManager(var project: Project) {
    private val tabbedPane = JBTabbedPane()

    init {
        createFixedTabs()
    }


    fun getTabbedPane(): JBTabbedPane {
        return tabbedPane
    }


    private fun createFixedTabs() {

        // Create the "Subscribe" tab
        val subscribeContent = SoyDi.inject(SubscribeView::class.java).getSubscribeContent(project)
        tabbedPane.addTab("Subscribe", createTabContent(subscribeContent))

        // Create the "Publish" tab
        val publishContent = PublishView().getPublishContent()
        tabbedPane.addTab("Publish", createTabContent(publishContent))
    }


    private fun createTabContent(content: JPanel): JPanel {
        val root = JPanel(BorderLayout())
        root.border = EmptyBorder(8, 16, 8, 16)

        // Wrap the provided content in a JBSplitter if needed (comment/uncomment depending on your needs)
        // val splitter = JBSplitter(false, 0.5f)
        // splitter.firstComponent = content // Replace with your content as needed
        // root.add(splitter, BorderLayout.CENTER)

        root.add(content, BorderLayout.CENTER)
        return root

    }
}
