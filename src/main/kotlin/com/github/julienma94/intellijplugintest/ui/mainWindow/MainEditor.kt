package com.github.julienma94.intellijplugintest.ui.mainWindow

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import java.beans.PropertyChangeListener
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel


@Service(Service.Level.PROJECT)
class MainEditor : FileEditor {

    init {
        thisLogger().info("MainEditor created")
    }


    override fun getName(): String {
        return "Main Editor"
    }

    override fun setState(p0: FileEditorState) {
        TODO("Not yet implemented")
    }

    override fun isModified(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isValid(): Boolean {
        TODO("Not yet implemented")
    }

    override fun addPropertyChangeListener(p0: PropertyChangeListener) {
        TODO("Not yet implemented")
    }

    override fun removePropertyChangeListener(p0: PropertyChangeListener) {
        TODO("Not yet implemented")
    }

    override fun getFile() = null

    override fun getPreferredFocusedComponent() = null
    override fun <T : Any?> getUserData(p0: Key<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> putUserData(p0: Key<T>, p1: T?) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        Disposer.dispose(this)
    }

    override fun getComponent(): JComponent {
        val tabContent = JPanel()
        tabContent.add(JButton("foo"))
        return tabContent
    }

}