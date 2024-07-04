package com.github.julienma94.intellijplugintest

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.di.Inject
import org.correomqtt.di.Observes
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

    fun incomingMessageEvent(@Observes event: IncomingMessageEvent) {
        println("Got message ${event.messageDTO.payload}")
    }

    fun init() {
        if (!isInitialized) {
            thisLogger().info("Correo App init")
            correoCore!!.init()
            isInitialized = true;
        }
    }
}
