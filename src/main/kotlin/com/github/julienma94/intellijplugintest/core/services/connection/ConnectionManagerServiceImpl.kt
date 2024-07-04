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

    private val subscribeTaskFactory: SubscribeTaskFactory = guiCore.getSubscriptionManager()

    private val publishTaskFactory: PublishTaskFactory = guiCore.getPublishTaskManager()

    private lateinit var connectedId: String;

    override fun getConnections(): List<ConnectionConfigDTO> {
        return settingsManager.connectionConfigs
    }

    override fun connect(connectionId: String) {
        guiCore.getConnectionLifecycleTaskFactory()
                .connectFactory
                .create(connectionId)
                .onSuccess {
                    connectedId = connectionId
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
    override fun subscribe(topic: String) {
        val subscribeDTO = SubscriptionDTO.builder()
            .topic(topic)
            .qos(Qos.AT_LEAST_ONCE)
            .build()

        println("Try subscribing to $topic with connectionId $connectedId")

        subscribeTaskFactory.create(connectedId, subscribeDTO).onSuccess {
            println("Succesfully subscribed to $topic")
        }.run()
    }

    override fun publish(topic: String, payload: String) {

        val messageDTO = MessageDTO.builder()
            .topic(topic)
            .payload(payload)
            .qos(Qos.AT_LEAST_ONCE)
            .messageId(UUID.randomUUID().toString())
            .messageType(MessageType.OUTGOING)
            .dateTime(LocalDateTime.now())
            .build()
        publishTaskFactory.create(connectedId, messageDTO).onSuccess {
            println("Publishing message on topic $topic")
        }.run()
    }
}