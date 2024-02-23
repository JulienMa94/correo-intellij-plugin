package com.github.julienma94.intellijplugintest.core.services.connection

import com.github.julienma94.intellijplugintest.CorreoPlugin
import com.github.julienma94.intellijplugintest.GuiCore
import com.intellij.openapi.diagnostic.thisLogger
import com.jetbrains.rd.util.string.printToString
import org.correomqtt.core.CorreoCore
import org.correomqtt.core.model.ConnectionConfigDTO
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.core.utils.ConnectionManager
import org.correomqtt.core.utils.CorreoMqttConnection
import org.correomqtt.di.Inject
import org.correomqtt.di.SoyDi

class ConnectionManagerServiceImpl : ConnectionManagerService {

    private val guiCore: GuiCore

    private val settingsManager: SettingsManager

    init {
        guiCore = SoyDi.inject(GuiCore::class.java)
        settingsManager = guiCore.getSettingsManager()
    }

    override fun getConnections(): List<ConnectionConfigDTO> {
        return settingsManager.connectionConfigs
    }

    override fun connect(connectionId: String) {
        TODO("NOt implemented yet")
    }
}