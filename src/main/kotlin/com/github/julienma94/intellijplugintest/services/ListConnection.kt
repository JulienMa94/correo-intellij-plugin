package com.github.julienma94.intellijplugintest.services

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowFactory

class ListConnection : AnAction() {

    init {
        thisLogger().warn("List all connections.")
    }

    override fun actionPerformed(p0: AnActionEvent) {
        Messages.showInfoMessage("Hello, World!", "Sample")
        thisLogger().info(p0.project?.name ?:"hello" )
    }
}