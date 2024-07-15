package com.github.julienma94.intellijplugintest.ui.publish

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import java.awt.Font
import javax.swing.JTextField

class PublishView {
    private val service = service<ConnectionManagerService>()

    public fun getPublishContent(): DialogPanel {
        return panel {
            group("Publish", true) {
                fun getEditorFont(colorsScheme: EditorColorsScheme): Font {
                    // Retrieve the editor font from the current scheme
                    val editorFont = colorsScheme.getFont(EditorFontType.PLAIN)
                    return Font(editorFont.family, Font.PLAIN, editorFont.size)
                }

                fun applyIntelliJTheme(textArea: RSyntaxTextArea) {
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


                fun createCompletionProvider(): CompletionProvider {
                    val provider = DefaultCompletionProvider()
                    return provider
                }

                fun createJsonTextArea(): RSyntaxTextArea {
                    val textArea = RSyntaxTextArea(20, 60)
                    textArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JSON
                    textArea.isCodeFoldingEnabled = true

                    val provider = createCompletionProvider()
                    val ac = AutoCompletion(provider)
                    ac.install(textArea)

                    applyIntelliJTheme(textArea)
                    return textArea
                }

                val topicTextField = JTextField()
                val textArea = createJsonTextArea()


                val subscribeAction = object : DumbAwareAction(
                    "Publishing to ${topicTextField.text}",
                    "Subscribing action",
                    AllIcons.Actions.Execute
                ) {
                    override fun actionPerformed(e: AnActionEvent) {
                        service.publish(topicTextField.text, textArea.text)
                    }
                }
                row {
                    cell(topicTextField).resizableColumn().align(AlignX.FILL)
                    actionButton(subscribeAction)
                }
                row {
                    cell(textArea).resizableColumn().align(AlignX.FILL)
                }
            }
        }
    }
}