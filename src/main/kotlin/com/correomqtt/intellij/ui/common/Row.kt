package com.correomqtt.intellij.ui.common

import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

open class Row(isSelected: Boolean): JPanel(BorderLayout()) {
    init {
        if (isSelected) {
            background = UIUtil.getListSelectionBackground(true) // Theme-aware selection color
            foreground = UIUtil.getListSelectionForeground(true) // Theme-aware selection text color
        } else {
            background = UIUtil.getListBackground() // Default background color
            foreground = UIUtil.getListForeground() // Default text color
        }

        minimumSize = Dimension(0, 48)
        maximumSize = Dimension(Int.MAX_VALUE, 48)
        border = JBUI.Borders.empty(8, 16)
    }
}