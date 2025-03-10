package com.correomqtt.intellij.ui.subscribe

import com.correomqtt.intellij.ui.common.Row
import com.intellij.icons.AllIcons
import com.intellij.util.ui.UIUtil
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.AlphaComposite
import java.awt.BorderLayout
import java.awt.Font
import java.awt.image.BufferedImage
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

class MessageItem(message: IncomingMessageEvent, isSelected: Boolean) {
    private val row = Row(isSelected)

    init {
        val topic = JLabel(message.messageDTO.topic)
        topic.font = topic.font.deriveFont(Font.BOLD)
        val timestamp = JLabel(message.messageDTO.dateTime.toString())
        val payload = JLabel(message.messageDTO.payload)

        val placeholderIcon = if (message.messageDTO.isRetained) createGreyIcon(AllIcons.Actions.Install) else AllIcons.Actions.Install
        val placeholderLabel = JLabel(placeholderIcon)

        row.addComponent(placeholderLabel, BorderLayout.WEST)
        row.addComponent(payload, BorderLayout.CENTER)
        row.addComponent(timestamp, BorderLayout.EAST)
    }

    fun createGreyIcon(originalIcon: Icon): Icon {
        val image = BufferedImage(originalIcon.iconWidth, originalIcon.iconHeight, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()

        // Draw the original icon
        originalIcon.paintIcon(null, g, 0, 0)

        // Apply a grey tint overlay
        g.composite = AlphaComposite.SrcAtop
        g.color = UIUtil.getInactiveTextColor()// Custom grey shade
        g.fillRect(0, 0, image.width, image.height)
        g.dispose()

        return ImageIcon(image)
    }

    fun getContent(): JPanel {
        return row.getContent()
    }
}