package com.github.julienma94.intellijplugintest.ui.common

import java.awt.Color
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JLabel
import javax.swing.JPanel

class DefaultPanel {

    fun getContent(msg: String): JPanel {
        val defaultPanel = JPanel()
        defaultPanel.layout = GridBagLayout()
        val constraints = GridBagConstraints()
        constraints.anchor = GridBagConstraints.CENTER
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        constraints.insets = Insets(10, 10, 10, 10)  // Adding some padding around the text

        val defaultLabel = JLabel(msg)
        defaultLabel.foreground = Color.GRAY

        defaultPanel.add(defaultLabel, constraints)

        return defaultPanel
    }
}