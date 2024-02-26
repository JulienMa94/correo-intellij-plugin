package com.github.julienma94.intellijplugintest.ui.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull


class PublishSubscribeProvider : FileEditorProvider, DumbAware {

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return true //will accept all kind of files, must be specified
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return PublishSubscribeEditor(project, file)
    }

    override fun getEditorTypeId(): String {
        return "CorreoPublishSubscribe"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }
}