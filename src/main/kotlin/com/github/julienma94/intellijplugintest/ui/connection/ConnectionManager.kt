package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.openapi.components.service
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import kotlin.reflect.KFunction1

class ConnectionManager {

    private val service = service<ConnectionManagerService>()
    private val tree: JTree = JTree()

    public fun initializeConnectionTree(onDoubleClick: KFunction1<String, Unit>): JTree {
        val connections = service.getConnections();

        // Tree setup
        val root = DefaultMutableTreeNode("MQTT")
        connections.forEach {
            val connection = DefaultMutableTreeNode(it.name)
            root.add(connection)
        }

        tree.model = DefaultTreeModel(root)


        // Add mouse listener to handle double-clicks
        tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val selectedNode = tree.lastSelectedPathComponent as? DefaultMutableTreeNode
                    if (selectedNode != null && selectedNode.isLeaf) {
                        val connection = connections.find {
                            it.name === selectedNode.userObject.toString()
                        }

                        if (connection != null) {
                            service.connect(connection.id)
                            val tabTitle = selectedNode.userObject.toString()
                            onDoubleClick(tabTitle)
                        }
                    }
                }
            }
        })

        return tree;
    }
}