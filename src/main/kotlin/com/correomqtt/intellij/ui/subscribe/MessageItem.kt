package com.correomqtt.intellij.ui.subscribe

import com.intellij.icons.AllIcons
import com.intellij.util.ui.UIUtil
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.border.EmptyBorder

//TODO Make message item height as heigh as topic item
class MessageItem(message: IncomingMessageEvent, isSelected: Boolean) {
    private val row = JPanel(BorderLayout())

    init {
        if (isSelected) {
            row.background = UIUtil.getListSelectionBackground(true) // Theme-aware selection color
            row.foreground = UIUtil.getListSelectionForeground(true) // Theme-aware selection text color
        } else {
            row.background = UIUtil.getListBackground() // Default background color
            row.foreground = UIUtil.getListForeground() // Default text color
        }

        row.border = EmptyBorder(8, 16, 8, 16)
        row.preferredSize = Dimension(0, 64)

        val topic = JLabel(message.messageDTO.topic)
        topic.font = topic.font.deriveFont(Font.BOLD)
        val timestamp = JLabel(message.messageDTO.dateTime.toString())
        val payload = JLabel(message.messageDTO.payload)

        val placeholderIcon = if (message.messageDTO.isRetained) createGreyIcon(AllIcons.Actions.Install) else AllIcons.Actions.Install
        val placeholderLabel = JLabel(placeholderIcon)

        row.add(placeholderLabel, BorderLayout.WEST)
        row.add(payload)
        row.add(timestamp, BorderLayout.EAST)
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
        return row
    }
}