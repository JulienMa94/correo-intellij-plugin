package com.github.julienma94.intellijplugintest.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import javax.swing.JInternalFrame

class ConnectAction : AnAction(){
    override fun actionPerformed(p0: AnActionEvent) {
        val project = p0.project
        Messages.showMessageDialog(project, "Hello, World!", "Greeting", Messages.getInformationIcon())

        val frame = JInternalFrame("Hello, World!")

        val label = JBLabel("Connect to the localhost")
    }
}