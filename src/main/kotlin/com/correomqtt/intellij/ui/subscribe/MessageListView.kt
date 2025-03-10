package com.correomqtt.intellij.ui.subscribe

import com.correomqtt.intellij.ui.connection.CONNECTION_SELECTED_TOPIC
import com.correomqtt.intellij.ui.connection.ConnectionSelectionListener
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.di.Assisted
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Observes
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

//TODO: Add message listener click to select message and display in message detail view
@DefaultBean
class MessageListView constructor (@Assisted project: Project) : JPanel(BorderLayout()) {
    private val listModel = DefaultListModel<IncomingMessageEvent>()
    private val jbList = JBList(listModel)

    init {
        val connection = project.messageBus.connect();

        connection.subscribe(CONNECTION_SELECTED_TOPIC, object : ConnectionSelectionListener {
            override fun onConnectionSelected(name: String, id: String) {
                println("Connection selected received in message list view: $name, $id")
            }
        })

        jbList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        jbList.cellRenderer = ListCellRenderer<IncomingMessageEvent> { _, value, _, isSelected, _ ->
            isOpaque = true;
            MessageItem(value, isSelected)
        }

        jbList.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {  // Double-click action
                    val selectedItem = jbList.selectedValue
                    if (selectedItem != null) {
                        JOptionPane.showMessageDialog(this@MessageListView, "You selected: $selectedItem")
                    }
                }
            }
        })

        // Wrap JBList in a scroll pane
        jbList.border = BorderFactory.createEmptyBorder()
        val scrollPane = JBScrollPane(jbList).apply {
            border = BorderFactory.createEmptyBorder()
        }
        add(scrollPane, BorderLayout.CENTER)
    }

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Received incoming message event for topic ${event.messageDTO.topic}")
        listModel.addElement(event)
    }
}