package com.github.julienma94.intellijplugintest.core.listeners

import com.github.julienma94.intellijplugintest.core.services.activation.ApplicationActivationService
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.IdeFrame

internal class ApplicationActivationListener : ApplicationActivationListener {

    private val service = service<ApplicationActivationService>()

    override fun applicationActivated(ideFrame: IdeFrame) {
        service.init()
    }
}
