package com.github.julienma94.intellijplugintest

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean
import org.correomqtt.di.SoyEvents

@SingletonBean
class CorreoApp {

    private var correoCore: CorreoCore? = null

    @Inject
    constructor(
            correoCore: CorreoCore
    ) {
        this.correoCore = correoCore
    }

    fun init() {
        thisLogger().info("Correo App init")
        correoCore!!.init()
    }
}