package com.github.julienma94.intellijplugintest

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean

@SingletonBean
class CorreoPlugin {
    private var correoCore: CorreoCore? = null

    private var isInitialized = false;

    @Inject
    constructor(
            correoCore: CorreoCore,
    ) {
        this.correoCore = correoCore
    }

    fun init() {
        if (!isInitialized) {
            thisLogger().info("Correo App init")
            correoCore!!.init()
            isInitialized = true;
        }
    }
}
