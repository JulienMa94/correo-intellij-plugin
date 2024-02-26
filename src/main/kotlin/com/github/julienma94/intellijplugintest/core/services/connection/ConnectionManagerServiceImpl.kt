package com.github.julienma94.intellijplugintest.core.services.connection

import com.github.julienma94.intellijplugintest.GuiCore
import org.correomqtt.core.model.ConnectionConfigDTO
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.di.SoyDi

class ConnectionManagerServiceImpl : ConnectionManagerService {

    private val guiCore: GuiCore = SoyDi.inject(GuiCore::class.java)

    private val settingsManager: SettingsManager = guiCore.getSettingsManager()

    override fun getConnections(): List<ConnectionConfigDTO> {
        return settingsManager.connectionConfigs
    }

    override fun connect(connectionId: String) {
        guiCore.getConnectionLifecycleTaskFactory()
                .connectFactory
                .create(connectionId)
                .onSuccess {
                    println("Successfully connected to $connectionId");
                }
                .onError {
                    println("Error while connecting to $connectionId")
                }
                .run()
    }

    override fun disconnect(connectionId: String) {
        guiCore.getConnectionLifecycleTaskFactory()
                .disconnectFactory
                .create(connectionId)
                .onSuccess {
                    println("Successfully disconnected for $connectionId");
                }
                .onError {
                    println("Error while disconnecting from $connectionId")
                }
                .run()
    }
}