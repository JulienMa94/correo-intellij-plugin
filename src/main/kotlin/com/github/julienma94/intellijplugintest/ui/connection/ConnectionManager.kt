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
import com.intellij.ui.JBSplitter
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.di.Observes
import org.correomqtt.di.SingletonBean
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import java.awt.BorderLayout
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ConnectionManager : ToolWindowFactory {


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow()

        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow() {

        private val service = service<ConnectionManagerService>()
        private val content: JPanel = JPanel(BorderLayout())
        private val tree: JTree = JTree()
        private val splitter: JBSplitter = JBSplitter(false, 0.2f)
        private val tabbedPane: JTabbedPane = JTabbedPane()

        init {
            initializeConnectionTree()

            // TabbedPane setup
            splitter.secondComponent = tabbedPane
            content.add(splitter, BorderLayout.CENTER)
        }

        private fun initializeConnectionTree() {
            val connections = service.getConnections();

            // Tree setup
            val root = DefaultMutableTreeNode("MQTT")
            connections.forEach {
                val connection = DefaultMutableTreeNode(it.name)
                root.add(connection)
            }

            tree.model = DefaultTreeModel(root)
            splitter.firstComponent = JScrollPane(tree)

            // Add mouse listener to handle double-clicks
            tree.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (e.clickCount == 2) {
                        val selectedNode = tree.lastSelectedPathComponent as? DefaultMutableTreeNode
                        if (selectedNode != null && selectedNode.isLeaf) {
                            val connection = connections.find {
                                it.name === selectedNode.userObject.toString()
                            }

                            if (connection != null) {
                                service.connect(connection.id)
                                val tabTitle = selectedNode.userObject.toString()
                                addTab(tabTitle)
                            }

                        }
                    }
                }
            })
        }

        private fun addTab(title: String) {
            // Check if tab with the same title already exists
            for (i in 0 until tabbedPane.tabCount) {
                if (tabbedPane.getTitleAt(i) == title) {
                    tabbedPane.selectedIndex = i
                    return
                }
            }

            // Create tab content
            val tabContent = JPanel(BorderLayout())
            tabContent.add(getTabContent())

            // Add tab with custom tab component
            tabbedPane.addTab(title, tabContent)
            val tabIndex = tabbedPane.indexOfComponent(tabContent)
            tabbedPane.setTabComponentAt(tabIndex, createTabComponent(title))

            // Select the new tab
            tabbedPane.selectedIndex = tabIndex
        }

        private fun getTabContent(): JBSplitter {
            val splitter: JBSplitter = JBSplitter(false, 0.5f)

            splitter.firstComponent = getSubscribeContent()
            splitter.secondComponent = getPublishContent()

            return splitter
        }

        private fun getSubscribeContent(): DialogPanel {
            return panel {
                group("Subscription", true) {

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

                    val textArea = createJsonTextArea()
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
                    row {
                        cell(textArea).resizableColumn().align(AlignX.FILL)
                    }
                }
            }
        }

        private fun getPublishContent(): DialogPanel {
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

        private fun createTabComponent(title: String): JPanel {
            val tabComponent = JPanel(BorderLayout())
            tabComponent.isOpaque = false

            val titleLabel = JLabel(title)
            val closeButton = JButton("x")

            closeButton.border = BorderFactory.createEmptyBorder()
            closeButton.isContentAreaFilled = false
            closeButton.addActionListener {
                val tabIndex = tabbedPane.indexOfTabComponent(tabComponent)
                if (tabIndex != -1) {
                    tabbedPane.remove(tabIndex)
                }
            }

            tabComponent.add(titleLabel, BorderLayout.CENTER)
            tabComponent.add(closeButton, BorderLayout.EAST)
            return tabComponent
        }

        fun getContent(): JPanel {
            return content
        }
    }
}
