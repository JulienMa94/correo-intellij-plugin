package com.github.julienma94.intellijplugintest.ui.subscribe

import com.github.julienma94.intellijplugintest.ui.connection.CONNECTION_SELECTED_TOPIC
import com.github.julienma94.intellijplugintest.ui.connection.ConnectionSelectionListener
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.core.pubsub.SubscribeEvent
import org.correomqtt.core.pubsub.UnsubscribeEvent
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Observes
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.DefaultListModel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.ListSelectionModel

@DefaultBean
class MessageListView() : JPanel(BorderLayout()) {

    private val listModel = DefaultListModel<String>()
    private val jbList = JBList(listModel)
    private var project: Project? = null

    init {
        jbList.selectionMode = ListSelectionModel.SINGLE_SELECTION
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

    fun addProject(project: Project) {
       if (this.project == null) {
           this.project.apply {
               this@MessageListView.project = project

                val connection = project.messageBus.connect();

                connection.subscribe(CONNECTION_SELECTED_TOPIC, object : ConnectionSelectionListener {
                    override fun onConnectionSelected(name: String, id: String) {
                        println("Connection selected received in message list view: $name, $id")
                    }
                })
           }
       }
    }

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Received incoming message event for topic ${event.messageDTO.topic}")
        listModel.addElement(event.messageDTO.payload)
    }
}