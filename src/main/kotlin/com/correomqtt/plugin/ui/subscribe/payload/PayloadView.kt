package com.correomqtt.plugin.ui.subscribe.payload

import com.correomqtt.plugin.ui.common.components.PayloadArea
import com.correomqtt.plugin.ui.common.events.ON_MESSAGE_SELECTED
import com.correomqtt.plugin.ui.common.events.MessageSelectionListener
import com.intellij.openapi.project.Project
import org.correomqtt.core.model.MessageDTO
import java.awt.BorderLayout
import javax.swing.JPanel

class PayloadView(project: Project) : JPanel(BorderLayout()) {
    init {
        val payloadArea = PayloadArea().createJsonTextArea()
        add(PayloadToolbar(project), BorderLayout.NORTH)
        add(payloadArea, BorderLayout.CENTER)

        val messageBus = project.messageBus.connect()

        messageBus.subscribe(ON_MESSAGE_SELECTED, object : MessageSelectionListener {
            override fun onMessageSelected(message: MessageDTO) {
                payloadArea.text = message.payload
            }
        })
    }
}