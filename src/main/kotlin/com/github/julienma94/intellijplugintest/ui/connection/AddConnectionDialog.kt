package com.github.julienma94.intellijplugintest.ui.connection

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class AddConnectionDialog(project: Project) : DialogWrapper(project) {
    private val nodeNameField = JTextField()

    init {
        init() // Initialize the dialog
        title = "Add Node"
        isOKActionEnabled = true
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.add(JPanel().apply {
            add(nodeNameField)
        }, BorderLayout.CENTER)
        return panel
    }

    fun getNodeName(): String {
        return nodeNameField.text
    }
}