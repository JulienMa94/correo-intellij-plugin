package com.github.julienma94.intellijplugintest.ui.menu

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.ui.components.JBPanel

class SettingsMenu: JBPanel<JBPanel<*>>(){

    // create a settings menu with a button to connect to the server and a test button

    init {
        thisLogger().info("SettingsMenu created")
    }
}