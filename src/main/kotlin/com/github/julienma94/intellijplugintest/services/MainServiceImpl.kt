package com.github.julienma94.intellijplugintest.services

import com.github.julienma94.intellijplugintest.CorreoApp
import com.github.julienma94.intellijplugintest.MyBundle
import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.di.SoyDi

class MainServiceImpl : MainService {

    private lateinit var app: CorreoApp

    override fun init() {
        thisLogger().info(MyBundle.message("start"))
        SoyDi.addClassLoader(CorreoApp::class.java.classLoader)
        SoyDi.scan("org.correomqtt", false)
        SoyDi.scan("com.github.julienma94", false);
        app = SoyDi.inject(CorreoApp::class.java)
        app.init()
    }

    override fun createConnection() {

    }
}
