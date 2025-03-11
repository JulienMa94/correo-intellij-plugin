package com.correomqtt.intellij.ui.common.events

import com.intellij.util.messages.Topic
import org.correomqtt.core.model.MessageDTO

interface ConnectionSelectionListener {
    fun onConnectionSelected(name: String, id: String)
}

interface MessageSelectionListener {
    fun onMessageSelected(message: MessageDTO)
}

val CONNECTION_SELECTED_TOPIC = Topic.create("Connection selected", ConnectionSelectionListener::class.java)
val MESSAGE_SELECTED = Topic.create("Message selected", MessageSelectionListener::class.java)
