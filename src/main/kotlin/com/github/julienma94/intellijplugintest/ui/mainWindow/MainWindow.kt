package com.github.julienma94.intellijplugintest.ui.mainWindow

import com.intellij.ui.components.JBPanel
import javax.swing.JFrame

class MainWindow(frame: JFrame?) : JBPanel<JBPanel<*>>() {

    // create a main window with a button to connect to the server and a test button
    init {
        println("MainWindow created")
    }
}