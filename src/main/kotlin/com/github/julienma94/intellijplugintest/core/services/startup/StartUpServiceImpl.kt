package com.github.julienma94.intellijplugintest.core.services.startup

import com.github.julienma94.intellijplugintest.core.CorreoPlugin
import org.correomqtt.core.utils.DirectoryUtils
import org.correomqtt.di.SoyDi

class StartUpServiceImpl : StartUpService {

    private lateinit var app: CorreoPlugin

    private var isScanned = false

    override fun init(configDirectoryBasePath: String) {
        if (!isScanned) {
            SoyDi.scan("org.correomqtt")
            SoyDi.scan("com.github.julienma94.intellijplugintest")
            isScanned = true
        }

        app = SoyDi.inject(CorreoPlugin::class.java)
        app.init("$configDirectoryBasePath/.idea")
    }

    override fun getBaseDirectoryPath(): String? {
        return DirectoryUtils.getTargetDirectoryPath()
    }
}
