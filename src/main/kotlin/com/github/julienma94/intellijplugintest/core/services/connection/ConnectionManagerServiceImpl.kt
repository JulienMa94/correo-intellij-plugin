package com.github.julienma94.intellijplugintest.core.services.connection

import com.github.julienma94.intellijplugintest.GuiCore
import org.correomqtt.core.model.*
import org.correomqtt.core.pubsub.PublishTaskFactory
import org.correomqtt.core.pubsub.SubscribeTaskFactory
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.di.SoyDi
import java.time.LocalDateTime
import java.util.*

class ConnectionManagerServiceImpl : ConnectionManagerService {

    private val guiCore: GuiCore = SoyDi.inject(GuiCore::class.java)

    private val settingsManager: SettingsManager = guiCore.getSettingsManager()

    private val tabIndexToConnectionId = mutableMapOf<Int, String>()

    private lateinit var activeConnectionId: String;

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

    override fun disconnect(tabIndex: Int) {
        val connectionId = tabIndexToConnectionId[tabIndex] ?: return

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

    override fun addConnectionId(tabIndex: Int, connectionId: String) {
        tabIndexToConnectionId[tabIndex] = connectionId
    }

    override fun removeConnectionId(tabIndex: Int) {
        tabIndexToConnectionId.remove(tabIndex)
    }

    override fun setActiveConnectionId(connectionId: String) {
        activeConnectionId = connectionId
    }

    override fun getActiveConnectionId(): String {
       return activeConnectionId
    }

}