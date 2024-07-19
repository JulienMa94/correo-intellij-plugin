package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import org.correomqtt.core.connection.ConnectionState
import org.correomqtt.core.connection.ConnectionStateChangedEvent
import org.correomqtt.core.model.ConnectionConfigDTO
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Observes
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel

@DefaultBean
class ConnectionTree() {

    private val service = service<ConnectionManagerService>()
    private val tree: JTree = JTree()
    private val connectionStateMap = mutableMapOf<String, ConnectionState>()

    fun onConnectionStateChanged(@Observes event: ConnectionStateChangedEvent) {
        println("Received connection state change event ${event.state}")
        connectionStateMap[event.connectionId] = event.state
        tree.revalidate()
        tree.repaint()
    }

    fun initializeConnectionTree(onDoubleClick: (String, String) -> Unit): JTree {
        val connections = service.getConnections();

        // Tree setup
        val root = DefaultMutableTreeNode("MQTT")
        connections.forEach {
            val connection = DefaultMutableTreeNode(it)
            root.add(connection)
        }

        tree.model = DefaultTreeModel(root)

        // Custom renderer for tree nodes
        tree.cellRenderer = object : DefaultTreeCellRenderer() {
            override fun getTreeCellRendererComponent(
                tree: JTree?,
                value: Any?,
                sel: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ): java.awt.Component {
                val component = super.getTreeCellRendererComponent(
                    tree, value, sel, expanded, leaf, row, hasFocus
                )
                openIcon = AllIcons.Actions.ListFiles
                closedIcon = AllIcons.Actions.ListFiles

                if (leaf && value is DefaultMutableTreeNode) {
                    val connectionInfo = value.userObject as? ConnectionConfigDTO
                    if (connectionInfo != null) {
                        // Create a custom panel to hold the text and the icon
                        val panel = JPanel(BorderLayout())
                        panel.isOpaque = false

                        // Create a label for the text
                        val labelText = "${connectionInfo.name} | ${connectionInfo.hostAndPort}"
                        val textLabel = JLabel(labelText)

                        // Add the label and the icon to the panel
                        panel.add(textLabel, BorderLayout.WEST)

                        val connectionState = connectionStateMap[connectionInfo.id]

                        if (connectionState == ConnectionState.CONNECTED) {
                            val checkLabel = JLabel(AllIcons.General.InspectionsOK)
                            panel.add(checkLabel, BorderLayout.EAST)
                        }

                        return panel
                    }
                }
                return component
            }
        }


        // Add mouse listener to handle double-clicks
        tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val selectedNode = tree.lastSelectedPathComponent as? DefaultMutableTreeNode
                    if (selectedNode != null && selectedNode.isLeaf) {
                        val nodeData = selectedNode.userObject as ConnectionConfigDTO?

                        val connection = connections.find {
                            it.name === nodeData?.name
                        }

                        if (connection != null) {
                            service.connect(connection.id)
                            if (nodeData != null) {
                                onDoubleClick(nodeData.name, connection.id)
                            }
                        }
                    }
                }
            }
        })

        return tree;
    }
}


