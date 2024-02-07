package com.github.julienma94.intellijplugintest.ui.mainWindow

import com.intellij.ui.components.JBPanel

class MainWindow: JBPanel<JBPanel<*>>() {

    // create a main window with a button to connect to the server and a test button
    init {
        println("MainWindow created")
    }
}