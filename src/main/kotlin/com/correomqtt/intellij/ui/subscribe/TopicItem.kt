package com.correomqtt.intellij.ui.subscribe

import com.intellij.icons.AllIcons
import com.intellij.util.ui.UIUtil
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.border.EmptyBorder

//TODO Add qos to topic item
class TopicItem(topic: String, isSelected: Boolean) {
    private val row = JPanel(BorderLayout())

    init {
        if (isSelected) {
            row.background = UIUtil.getListSelectionBackground(true) // Theme-aware selection color
            row.foreground = UIUtil.getListSelectionForeground(true) // Theme-aware selection text color
        } else {
            row.background = UIUtil.getListBackground() // Default background color
            row.foreground = UIUtil.getListForeground() // Default text color
        }

        row.border = EmptyBorder(8, 8, 8, 8)

        // Right: Unsubscribe Button with Cross Icon
        val unsubscribeButton = JButton(AllIcons.Actions.Close).apply {
            isBorderPainted = false
            isContentAreaFilled = false
            toolTipText = "Unsubscribe"
            addActionListener { } // Call unsubscribe function
            preferredSize = Dimension(16, 16)
        }

        // Layout adjustments
        val leftPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            isOpaque = false
            add(JLabel(topic))
        }

        val rightPanel = JPanel(FlowLayout(FlowLayout.RIGHT)).apply {
            isOpaque = false
            add(JLabel("Qos 0"))
            add(unsubscribeButton)
        }

        row.add(leftPanel, BorderLayout.WEST)
        row.add(rightPanel, BorderLayout.EAST)
    }



    fun getContent(): JPanel {
        return row
    }
}