package com.github.julienma94.intellijplugintest.core.services.activation

import com.github.julienma94.intellijplugintest.CorreoPlugin
import com.github.julienma94.intellijplugintest.MyBundle
import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.di.SoyDi

class ApplicationActivationServiceImpl : ApplicationActivationService {

    private lateinit var app: CorreoPlugin

    private var isInitialized = false

    override fun init() {
       if (!isInitialized) {
           thisLogger().info(MyBundle.message("start"))
           SoyDi.scan("org.correomqtt")
           SoyDi.scan("com.github.julienma94");
           app = SoyDi.inject(CorreoPlugin::class.java)
           app.init()
           isInitialized = true
       }
    }
}
