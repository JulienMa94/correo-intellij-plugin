package com.github.julienma94.intellijplugintest

import org.correomqtt.core.CoreManager
import org.correomqtt.core.connection.ConnectionLifecycleTaskFactories
import org.correomqtt.core.fileprovider.HistoryManager
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.core.utils.ConnectionManager
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean


@SingletonBean
class GuiCore {

    private var coreManager: CoreManager
    private var connectionManager: ConnectionManager
    private var settingsManager: SettingsManager
    private var historyManager: HistoryManager
    private var connectionLifecycleTaskFactories: ConnectionLifecycleTaskFactories

    @Inject
    constructor(
            coreManager: CoreManager,
            connectionLifecycleTaskFactories: ConnectionLifecycleTaskFactories
    ) {
        this.connectionLifecycleTaskFactories = connectionLifecycleTaskFactories
        this.coreManager = coreManager
        connectionManager = coreManager.connectionManager
        settingsManager = coreManager.settingsManager
        historyManager = coreManager.historyManager
    }

    fun getConnectionManager(): ConnectionManager {
        return this.connectionManager;
    }

    fun getSettingsManager(): SettingsManager {
        return this.settingsManager;
    }

    fun getHistoryManager(): HistoryManager {
        return this.historyManager;
    }

    fun getConnectionLifecycleTaskFactory(): ConnectionLifecycleTaskFactories {
        return this.connectionLifecycleTaskFactories;
    }
}