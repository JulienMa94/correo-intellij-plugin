package com.correomqtt.intellij.ui.tab

import com.correomqtt.intellij.ui.publish.PublishView
import com.correomqtt.intellij.ui.subscribe.SubscribeViewFactory
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import com.intellij.util.ui.JBUI
import org.correomqtt.di.Assisted
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Inject
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTabbedPane

@DefaultBean
class TabManager @Inject constructor(@Assisted var project: Project): JTabbedPane() {
    init {
        // Create the "Subscribe" tab
        val subscribeContent = SubscribeViewFactory().create(project)
        addTab("Subscribe", createTabContent(subscribeContent))

        // Create the "Publish" tab
        val publishContent = PublishView().getPublishContent()
        addTab("Publish", createTabContent(publishContent))
    }

    private fun createTabContent(content: JPanel): JPanel {
        val root = JPanel(BorderLayout())
        root.border = JBUI.Borders.empty(8, 16)

        // Wrap the provided content in a JBSplitter if needed (comment/uncomment depending on your needs)
        // val splitter = JBSplitter(false, 0.5f)
        // splitter.firstComponent = content // Replace with your content as needed
        // root.add(splitter, BorderLayout.CENTER)

        root.add(content, BorderLayout.CENTER)
        return root
    }
}
