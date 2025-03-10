package com.correomqtt.intellij

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean

@SingletonBean
class CorreoPlugin @Inject constructor(correoCore: CorreoCore) {
    private var correoCore: CorreoCore? = correoCore
    private var isInitialized = false;

    fun init() {
        if (!isInitialized) {
            thisLogger().info("Correo App init")
            correoCore!!.init()
            isInitialized = true;
        }
    }
}
