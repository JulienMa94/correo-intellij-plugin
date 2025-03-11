package com.correomqtt.intellij

import com.github.julienma94.intellijplugintest.core.services.secruity.KeyringManager
import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean

@SingletonBean
class CorreoPlugin @Inject constructor(
    private var correoCore: CorreoCore,
    private var keyringManager: KeyringManager
) {
    private var isInitialized = false;

    fun init() {
        if (!isInitialized) {
            thisLogger().info("Correo App init")
            correoCore!!.init()
            keyringManager.init()
            isInitialized = true;
        }
    }
}
