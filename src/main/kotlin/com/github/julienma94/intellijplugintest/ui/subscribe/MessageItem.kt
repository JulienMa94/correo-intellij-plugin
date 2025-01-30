package com.github.julienma94.intellijplugintest.ui.subscribe

import com.intellij.util.ui.UIUtil
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

//TODO Must be refactored to new figma design
class MessageItem(message: IncomingMessageEvent) {
    private val row = JPanel(BorderLayout())

    init {
        row.background = UIUtil.getPanelBackground()
        row.border = BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtil.getBoundsColor())

        val rowHeader = JPanel(BorderLayout())
        rowHeader.border = EmptyBorder(16, 8, 8, 8)

        val rowBody = JPanel(BorderLayout())
        rowBody.border = EmptyBorder(8, 8, 16, 8)

        val topic = JLabel(message.messageDTO.topic)
        topic.font = topic.font.deriveFont(Font.BOLD)
        val qos = JLabel(message.messageDTO.qos.toString())
        val retained = JLabel(message.messageDTO.isRetained.toString())
        val messageId = JLabel(message.messageDTO.messageId.toString())
        val timestamp = JLabel(message.messageDTO.dateTime.toString())

        rowHeader.add(messageId)
        rowHeader.add(timestamp)
        rowHeader.add(retained)
        rowHeader.add(qos)

        val payloadContainer = JPanel(BorderLayout())
        payloadContainer.background = UIUtil.getBoundsColor()
        payloadContainer.border = EmptyBorder(8, 16, 8, 16)
        val payload = JLabel(message.messageDTO.payload)
        payloadContainer.add(payload)

        rowBody.add(payloadContainer)

        row.add(rowHeader, BorderLayout.NORTH)
        row.add(rowBody, BorderLayout.CENTER)
    }

    fun getContent(): JPanel {
        return row
    }
}