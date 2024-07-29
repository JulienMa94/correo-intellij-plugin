package com.github.julienma94.intellijplugintest.ui.subscribe

import com.intellij.openapi.editor.colors.EditorColorsManager
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class MessageItem {
    public fun getContent(message: IncomingMessageEvent, width: Int): JPanel {
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        val customBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, colorsScheme.defaultBackground)

        val row = JPanel(BorderLayout())
        row.border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)
        row.background = colorsScheme.defaultBackground
        // Add a line border at the bottom of the row, except for the last row

        row.preferredSize = Dimension(width, 100)
        row.border = null
        row.maximumSize = Dimension(width, 100)
        row.minimumSize = Dimension(width, 100)

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
        payloadContainer.background = colorsScheme.defaultBackground
        payloadContainer.border = EmptyBorder(8, 16, 8, 16)
        val payload = JLabel(message.messageDTO.payload)
        payloadContainer.add(payload)

        rowBody.add(payloadContainer)

        row.border = customBorder
        row.add(rowHeader, BorderLayout.NORTH)
        row.add(rowBody, BorderLayout.CENTER)

        return row;
    }
}