package com.github.julienma94.intellijplugintest.services

import com.github.julienma94.intellijplugintest.CorreoApp
import com.github.julienma94.intellijplugintest.MyBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import org.correomqtt.di.SoyDi

class MainServiceImpl : MainService {

    private lateinit var app: CorreoApp

    override fun init() {
        thisLogger().info(MyBundle.message("start"))
        SoyDi.scan("org.correomqtt", false)
        SoyDi.scan("com.github.julienma94", false);
        app = SoyDi.inject(CorreoApp::class.java)
        app.init()
    }
}
