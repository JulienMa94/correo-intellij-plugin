package com.correomqtt.intellij.ui.subscribe

import com.correomqtt.intellij.ui.common.Row
import com.intellij.icons.AllIcons
import com.intellij.util.ui.UIUtil
import org.correomqtt.core.pubsub.IncomingMessageEvent
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.border.EmptyBorder

//TODO Add qos to topic item
class TopicItem(topic: String, isSelected: Boolean) {
    private val row = Row(isSelected)

    init {
        val unsubscribeButton = JButton(AllIcons.Actions.Close).apply {
            isBorderPainted = false
            isContentAreaFilled = false
            toolTipText = "Unsubscribe"
            addActionListener { } // Call unsubscribe function
            preferredSize = Dimension(16, 16)
        }

        row.addComponent(JLabel(topic), BorderLayout.WEST)
        row.addComponent(JLabel("Qos 0"), BorderLayout.CENTER)
        row.addComponent(unsubscribeButton, BorderLayout.EAST)
    }



    fun getContent(): JPanel {
        return row.getContent()
    }
}