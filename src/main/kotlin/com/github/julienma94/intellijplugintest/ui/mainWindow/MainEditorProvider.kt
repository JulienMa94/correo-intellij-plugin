package com.github.julienma94.intellijplugintest.ui.mainWindow

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MainEditorProvider : FileEditorProvider {
    override fun accept(p0: Project, p1: VirtualFile): Boolean {
        return true
    }

    override fun createEditor(p0: Project, p1: VirtualFile): FileEditor {
        return MainEditor()
    }

    override fun getEditorTypeId(): String {
        return "MainEditor"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }

}