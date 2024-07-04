package com.github.julienma94.intellijplugintest

import org.correomqtt.core.CoreManager
import org.correomqtt.core.connection.ConnectionLifecycleTaskFactories
import org.correomqtt.core.fileprovider.HistoryManager
import org.correomqtt.core.pubsub.PublishTaskFactory
import org.correomqtt.core.pubsub.SubscribeTaskFactory
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
    private var publishTaskFactory: PublishTaskFactory
    private var subscribeTaskFactory: SubscribeTaskFactory

    @Inject
    constructor(
        coreManager: CoreManager,
        connectionLifecycleTaskFactories: ConnectionLifecycleTaskFactories,
        publishTaskFactory: PublishTaskFactory,
        subscribeTaskFactory: SubscribeTaskFactory
    ) {
        this.connectionLifecycleTaskFactories = connectionLifecycleTaskFactories
        this.publishTaskFactory = publishTaskFactory
        this.subscribeTaskFactory = subscribeTaskFactory
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

    fun getSubscriptionManager(): SubscribeTaskFactory {
        return this.subscribeTaskFactory;
    }

    fun getPublishTaskManager(): PublishTaskFactory {
        return this.publishTaskFactory;
    }

    fun getConnectionLifecycleTaskFactory(): ConnectionLifecycleTaskFactories {
        return this.connectionLifecycleTaskFactories;
    }
}