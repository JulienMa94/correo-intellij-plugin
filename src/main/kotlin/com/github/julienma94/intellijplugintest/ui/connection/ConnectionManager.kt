package com.github.julienma94.intellijplugintest.ui.connection

import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.autocomplete.*
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice
import org.json.JSONObject
import org.json.JSONException
import java.awt.Font
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.text.html.parser.Parser


class ConnectionManager : ToolWindowFactory {

    private val connectionManagerService = service<ConnectionManagerService>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val myToolWindow = MyToolWindow(toolWindow)
        myToolWindow.setService(connectionManagerService)

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private lateinit var service: ConnectionManagerService;

        fun setService(service: ConnectionManagerService) {
            this.service = service
        }

        fun getPanel(init: (JPanel) -> Unit): JPanel {
            val panel = JPanel()
            init(panel)
            return panel
        }


        fun getContent(): DialogPanel {
            val connections = service.getConnections()

            val panel = panel {
                group("Connections", true) {
                    connections.forEach {
                        row {
                            text(it.name).bold()
                        }
                        row(it.hostAndPort) {
                            val connectionAction = object : DumbAwareAction(
                                "Connect to ${it.name}",
                                "Connect to ${it.name}",
                                AllIcons.Actions.Execute
                            ) {
                                override fun actionPerformed(e: AnActionEvent) {
                                    service.connect(it.id)
                                }
                            }
                            val disconnectAction = object : DumbAwareAction(
                                "Disconnect to ${it.name}",
                                "Disconnect to ${it.name}",
                                AllIcons.Actions.Cancel
                            ) {
                                override fun actionPerformed(e: AnActionEvent) {
                                    service.disconnect(it.id)
                                }
                            }
                            actionButton(connectionAction)
                            actionButton(disconnectAction)
                        }
                        separator()
                    }
                }
                group("Subscription", true) {
                    row {
                        val textField = JTextField()

                        val subscribeAction = object : DumbAwareAction(
                            "Subscribing to ${textField.text}",
                            "Subscribing action",
                            AllIcons.Actions.Execute
                        ) {
                            override fun actionPerformed(e: AnActionEvent) {
                                service.subscribe(textField.text)
                            }
                        }

                        cell(textField).resizableColumn().align(AlignX.FILL)
                        actionButton(subscribeAction)
                    }
                }
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
            }.apply {
                border = JBUI.Borders.empty(10, 10, 10, 10)  // Set padding: top, left, bottom, right
            }

            return panel
        }
    }
}
