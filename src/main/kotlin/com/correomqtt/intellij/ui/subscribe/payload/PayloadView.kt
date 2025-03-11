package com.correomqtt.intellij.ui.subscribe.payload

import com.correomqtt.intellij.ui.common.components.PayloadArea
import com.correomqtt.intellij.ui.common.events.MESSAGE_SELECTED
import com.correomqtt.intellij.ui.common.events.MessageSelectionListener
import com.intellij.openapi.project.Project
import org.correomqtt.core.model.MessageDTO
import java.awt.BorderLayout
import javax.swing.JPanel

class PayloadView(project: Project) : JPanel(BorderLayout()) {
    init {
        val payloadArea = PayloadArea().createJsonTextArea()
        add(payloadArea, BorderLayout.CENTER)

        val messageBus = project.messageBus.connect()

        messageBus.subscribe(MESSAGE_SELECTED, object : MessageSelectionListener {
            override fun onMessageSelected(message: MessageDTO) {
                payloadArea.text = message.payload
            }
        })
    }
}