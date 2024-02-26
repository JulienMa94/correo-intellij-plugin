package com.github.julienma94.intellijplugintest.ui.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.beans.PropertyChangeListener
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import com.intellij.openapi.util.UserDataHolderBase

internal class PublishSubscribeEditor(project: Project, virtualFile: VirtualFile) :
        UserDataHolderBase(), FileEditor {


    override fun getComponent(): JComponent {
        val tabContent = JPanel()
        tabContent.add(JButton("CorreoPublishSubscribe"))
        return tabContent
    }

    @Nullable
    override fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @NotNull
    override fun getName(): String {
        return "name"
    }

    override fun setState(@NotNull fileEditorState: FileEditorState) {}
    override fun isModified(): Boolean {
        return false
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun addPropertyChangeListener(@NotNull propertyChangeListener: PropertyChangeListener) {}
    override fun removePropertyChangeListener(@NotNull propertyChangeListener: PropertyChangeListener) {}

    @Nullable
    override fun getCurrentLocation(): FileEditorLocation? {
        return null
    }

    override fun dispose() {
        Disposer.dispose(this)
    }
    @Nullable
    override fun getFile(): VirtualFile {
        return file
    }
}