package com.github.julienma94.intellijplugintest.core

import com.github.julienma94.intellijplugintest.core.services.secruity.KeyringManager
import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.di.DefaultBean
import org.correomqtt.di.Inject
import org.correomqtt.di.SoyDi

@DefaultBean
class CorreoPlugin @Inject constructor(
    private var correoCore: CorreoCore,
) {

    fun init(configDirectoryBasePath: String) {
        thisLogger().info("Correo App init")
        thisLogger().info("Correo intellij project path: $configDirectoryBasePath")
        correoCore.init(configDirectoryBasePath)

        val keyringManager = SoyDi.inject(KeyringManager::class.java)

        try {
            keyringManager.init()
        } catch (e: Exception) {
            thisLogger().info("Error while init keyring manager")
        }
    }
}
