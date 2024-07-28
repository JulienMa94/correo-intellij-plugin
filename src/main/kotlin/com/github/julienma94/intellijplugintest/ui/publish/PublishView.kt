package com.github.julienma94.intellijplugintest.ui.publish

import com.github.julienma94.intellijplugintest.core.services.publish.PublishService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.DumbAwareAction
import org.correomqtt.core.model.Qos
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class PublishView {
    private val publishService = service<PublishService>()
    private val content = JPanel(BorderLayout())

    private fun getEditorFont(colorsScheme: EditorColorsScheme): Font {
        // Retrieve the editor font from the current scheme
        val editorFont = colorsScheme.getFont(EditorFontType.PLAIN)
        return Font(editorFont.family, Font.PLAIN, editorFont.size)
    }

    private fun applyIntelliJTheme(textArea: RSyntaxTextArea) {
        // Get the current editor colors scheme
        val colorsScheme = EditorColorsManager.getInstance().globalScheme

        // Apply colors and fonts from IntelliJ theme to RSyntaxTextArea
        textArea.background = colorsScheme.defaultBackground
        textArea.foreground = colorsScheme.defaultForeground
        textArea.font = getEditorFont(colorsScheme)

        // Apply caret color
        textArea.caretColor = colorsScheme.getColor(EditorColors.CARET_COLOR)

        // Apply selection color
        textArea.selectionColor = colorsScheme.getColor(EditorColors.SELECTION_BACKGROUND_COLOR)
        textArea.selectedTextColor = colorsScheme.getColor(EditorColors.SELECTION_FOREGROUND_COLOR)
        textArea.currentLineHighlightColor = colorsScheme.getColor(EditorColors.CARET_ROW_COLOR)
    }

    private fun createCompletionProvider(): CompletionProvider {
        val provider = DefaultCompletionProvider()
        return provider
    }

    private fun createJsonTextArea(): RSyntaxTextArea {
        val textArea = RSyntaxTextArea()
        textArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
        textArea.isCodeFoldingEnabled = true

        val provider = createCompletionProvider()
        val ac = AutoCompletion(provider)
        ac.install(textArea)

        applyIntelliJTheme(textArea)
        return textArea
    }

    fun getPublishContent(): JPanel {
        content.layout = BorderLayout()

        val textArea = createJsonTextArea()
        val rowScrollPane = JScrollPane(textArea)
        rowScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        rowScrollPane.preferredSize = Dimension(content.width, content.height)
        rowScrollPane.border = EmptyBorder(8, 2, 8, 2)

        val topicTextField = JTextField()
        topicTextField.preferredSize = Dimension(350, topicTextField.preferredSize.height) // Set fixed width


        val comboBox = JComboBox(arrayOf(Qos.AT_MOST_ONCE, Qos.AT_LEAST_ONCE, Qos.EXACTLY_ONCE))
        comboBox.preferredSize = Dimension(20, comboBox.preferredSize.height) // Set fixed width

        val publishAction = object : DumbAwareAction(
            "Publishing to ${topicTextField.text}",
            "Subscribing action",
            AllIcons.Actions.Execute
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                publishService.publish(topicTextField.text, textArea.text, comboBox.selectedItem as Qos)
            }
        }

        val publishButton = JButton("Publish")
        publishButton.addActionListener {
            publishAction.actionPerformed(
                AnActionEvent.createFromAnAction(
                    publishAction,
                    null,
                    "",
                    DataContext.EMPTY_CONTEXT
                )
            )
        }


        // Set the layout and constraints
        val gridBagLayout = GridBagLayout()
        val constraints = GridBagConstraints()
        val panel = JPanel(gridBagLayout)
        // Add the JTextField to the panel
        constraints.weightx = 0.7
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        panel.add(topicTextField, constraints)


        constraints.gridx = 1
        panel.add(comboBox, constraints)

        constraints.gridx = 2
        panel.add(publishButton, constraints)

        content.add(panel, BorderLayout.NORTH)
        content.add(rowScrollPane, BorderLayout.CENTER)

        return content;

    }
}