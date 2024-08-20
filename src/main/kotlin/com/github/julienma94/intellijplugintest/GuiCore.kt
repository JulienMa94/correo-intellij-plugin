package com.github.julienma94.intellijplugintest

import org.correomqtt.core.CoreManager
import org.correomqtt.core.connection.ConnectionLifecycleTaskFactories
import org.correomqtt.core.fileprovider.HistoryManager
import org.correomqtt.core.fileprovider.SecretStoreProvider
import org.correomqtt.core.keyring.KeyringFactory
import org.correomqtt.core.pubsub.PublishTaskFactory
import org.correomqtt.core.pubsub.SubscribeTaskFactory
import org.correomqtt.core.settings.SettingsManager
import org.correomqtt.core.utils.ConnectionManager
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean


@SingletonBean
class GuiCore @Inject constructor(
    private var coreManager: CoreManager,
    private var connectionLifecycleTaskFactories: ConnectionLifecycleTaskFactories,
    private var publishTaskFactory: PublishTaskFactory,
    private var subscribeTaskFactory: SubscribeTaskFactory,
    private var keyringFactory: KeyringFactory,
    private var secretStoreProvider: SecretStoreProvider
) {

    private var connectionManager: ConnectionManager = coreManager.connectionManager
    private var settingsManager: SettingsManager = coreManager.settingsManager
    private var historyManager: HistoryManager = coreManager.historyManager

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

    fun getSecretStoreProvider(): SecretStoreProvider {
        return this.secretStoreProvider;
    }

    fun getKeyringFactory(): KeyringFactory {
        return this.keyringFactory;
    }
}