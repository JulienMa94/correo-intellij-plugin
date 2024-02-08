package com.github.julienma94.intellijplugintest.listeners

import com.github.julienma94.intellijplugintest.services.MainService
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.wm.IdeFrame

internal class MyApplicationActivationListener : ApplicationActivationListener {

    private val service = service<MainService>()

    override fun applicationActivated(ideFrame: IdeFrame) {
        service.init()
    }
}
