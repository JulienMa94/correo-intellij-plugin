package com.github.julienma94.intellijplugintest

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.di.Inject
import org.correomqtt.di.Observes
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
