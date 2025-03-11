package com.correomqtt.intellij.ui.common.events

import com.intellij.util.messages.Topic
import org.correomqtt.core.model.MessageDTO
import org.correomqtt.core.model.Qos

interface ConnectionSelectionListener {
    fun onConnectionSelected(name: String, id: String)
}

interface MessageSelectionListener {
    fun onMessageSelected(message: MessageDTO)
}

interface PublishListener {
    fun onPublishListener(topic: String, qos: Qos, retained: Boolean)
}

val ON_CONNECTION_SELECTED_TOPIC = Topic.create("Connection selected", ConnectionSelectionListener::class.java)
val ON_MESSAGE_SELECTED = Topic.create("Message selected", MessageSelectionListener::class.java)
val ON_PUBLISH = Topic.create("Publish click", PublishListener::class.java)