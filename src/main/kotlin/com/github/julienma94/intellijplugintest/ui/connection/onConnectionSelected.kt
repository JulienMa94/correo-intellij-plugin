package com.github.julienma94.intellijplugintest.ui.connection

import com.intellij.util.messages.Topic

interface ConnectionSelectionListener {
    fun onConnectionSelected(name: String, id: String)
}

val CONNECTION_SELECTED_TOPIC = Topic.create("Connection selected", ConnectionSelectionListener::class.java)
