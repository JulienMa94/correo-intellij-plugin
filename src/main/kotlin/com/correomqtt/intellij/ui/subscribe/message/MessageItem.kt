package com.correomqtt.intellij.ui.subscribe.message

import com.correomqtt.intellij.ui.common.components.Row
import com.correomqtt.intellij.ui.util.IconHelper
import com.correomqtt.intellij.ui.util.TimestampHelper
import com.intellij.icons.AllIcons
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Font
import javax.accessibility.AccessibleContext
import javax.swing.JLabel
import javax.swing.JPanel

class MessageItem(message: IncomingMessageEvent, isSelected: Boolean) : Row(isSelected) {
    init {
        val topic = JLabel(message.messageDTO.topic)
        topic.font = topic.font.deriveFont(Font.BOLD)
        val timestamp = JLabel(TimestampHelper().format(message.messageDTO.dateTime.toString()))
        val payload = JLabel(message.messageDTO.payload)

        val retainedIcon = if (message.messageDTO.isRetained) {
            AllIcons.Actions.Install
        } else {
            IconHelper.createGreyIcon(AllIcons.Actions.Install)
        }
        val placeholderLabel = JLabel(retainedIcon)

        // Create a container panel for the WEST side to hold both placeholderLabel and payload
        val westPanel = JPanel(FlowLayout(FlowLayout.LEFT, 16, 0)) // Adds horizontal gap of 16px between components
        westPanel.isOpaque = false // Make sure the panel is transparent to show background

        // Add the placeholder label and payload label to the west panel
        westPanel.add(placeholderLabel)
        westPanel.add(payload)

        add(westPanel, BorderLayout.WEST)
        add(timestamp, BorderLayout.EAST)
    }

    override fun getAccessibleContext(): AccessibleContext {
        return super.getAccessibleContext()
    }
}