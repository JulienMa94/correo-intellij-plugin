package com.correomqtt.intellij.ui.common

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

class Row(isSelected: Boolean) {

    private val row = JPanel(BorderLayout())

    init {
        if (isSelected) {
            row.background = UIUtil.getListSelectionBackground(true) // Theme-aware selection color
            row.foreground = UIUtil.getListSelectionForeground(true) // Theme-aware selection text color
        } else {
            row.background = UIUtil.getListBackground() // Default background color
            row.foreground = UIUtil.getListForeground() // Default text color
        }

        row.preferredSize = Dimension(0, 48)
        row.border = EmptyBorder(8, 16, 8, 16)
    }

    fun addComponent(component: JComponent, constraints: Any) {
        row.add(component, constraints)
    }

    fun getContent(): JPanel {
        return row
    }
}