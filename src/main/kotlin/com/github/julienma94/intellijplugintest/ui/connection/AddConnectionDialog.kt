package com.github.julienma94.intellijplugintest.ui.connection

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.GridBag
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class AddConnectionDialog(project: Project) : DialogWrapper(project) {
    private val nameField = JBTextField()
    private val urlField = JBTextField()
    private val portField = JBTextField()
    private val clientIdField = JBTextField()
    private val userNameField = JBTextField()
    private val passwordField = JBTextField()
    private val cleanSessionField = JBCheckBox()
    private val mqttVersionField = ComboBox<String>().apply {
        addItem("3.1.1")
        addItem("5.0")
    }

    init {
        init() // Initialize the dialog
        title = "Add Correo Connection"
        isOKActionEnabled = true
        setSize(600, 400)
    }

    override fun createCenterPanel(): JComponent {
        val root = JPanel(BorderLayout())
        val panel = JPanel(GridBagLayout())
        val gb = GridBag()

        val checkBoxContainer = JPanel(BorderLayout())
        cleanSessionField.border = EmptyBorder(0, 0, 0, 8)
        checkBoxContainer.add(cleanSessionField, BorderLayout.WEST)
        checkBoxContainer.add(JBLabel("Clean Session"))

        // Add rows with labels and text fields
        addRow(panel, gb, "Name", nameField)
        addRow(panel, gb, "URL", urlField)
        addRow(panel, gb, "Port", portField)
        addRow(panel, gb, "Client ID", clientIdField)
        addRow(panel, gb, "Username", userNameField)
        addRow(panel, gb, "Password", passwordField)
        addRow(panel, gb, "MQTT-Version", mqttVersionField)

        panel.add(JPanel(), gb.nextLine().next().weightx(0.2).anchor(GridBagConstraints.WEST).insetBottom(16))
        panel.add(checkBoxContainer, gb.next().weightx(0.7).fillCellHorizontally().insetBottom(16))

        root.add(panel, BorderLayout.NORTH)

        return root
    }

    private fun addRow(panel: JPanel, gb: GridBag, labelText: String, component: JComponent, switch: Boolean = false) {
        panel.add(JBLabel(labelText), gb.nextLine().next().weightx(0.2).anchor(GridBagConstraints.WEST).insetBottom(16))
        panel.add(component, gb.next().weightx(0.7).fillCellHorizontally().insetBottom(16))
    }

    fun getNodeName(): String {
        return nameField.text
    }
}