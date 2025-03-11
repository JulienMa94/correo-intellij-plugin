package com.correomqtt.intellij.core.services.activation

import com.correomqtt.intellij.CorreoPlugin
import com.correomqtt.intellij.MyBundle
import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.di.SoyDi

class ApplicationActivationServiceImpl : ApplicationActivationService {

    private lateinit var app: CorreoPlugin
    private var isInitialized = false

    override fun init() {
        if (!isInitialized) {
            thisLogger().info(MyBundle.message("start"))
            SoyDi.scan("org.correomqtt")
            SoyDi.scan("com.correomqtt.intellij");
            SoyDi.scan("com.intellij.openapi.project")
            app = SoyDi.inject(CorreoPlugin::class.java)
            app.init()

            isInitialized = true
        }
    }
}
