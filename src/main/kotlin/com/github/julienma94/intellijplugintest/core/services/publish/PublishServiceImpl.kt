package com.github.julienma94.intellijplugintest.core.services.publish

import com.github.julienma94.intellijplugintest.GuiCore
import com.github.julienma94.intellijplugintest.core.services.connection.ConnectionManagerService
import com.intellij.openapi.components.service
import org.correomqtt.core.model.MessageDTO
import org.correomqtt.core.model.MessageType
import org.correomqtt.core.model.Qos
import org.correomqtt.core.pubsub.PublishTaskFactory
import org.correomqtt.di.SoyDi
import java.time.LocalDateTime
import java.util.*

class PublishServiceImpl : PublishService{
    private val guiCore: GuiCore = SoyDi.inject(GuiCore::class.java)
    private val connectionService = service<ConnectionManagerService>()

    private val publishTaskFactory: PublishTaskFactory = guiCore.getPublishTaskManager()

    override fun publish(topic: String, payload: String, qos: Qos) {
        val messageDTO = MessageDTO.builder()
            .topic(topic)
            .payload(payload)
            .qos(qos)
            .messageId(UUID.randomUUID().toString())
            .messageType(MessageType.OUTGOING)
            .dateTime(LocalDateTime.now())
            .build()

        val activeConnectionId = connectionService.getActiveConnectionId()

        publishTaskFactory.create(activeConnectionId, messageDTO).onSuccess {
            println("Publishing message on topic $topic")
        }.run()
    }
}