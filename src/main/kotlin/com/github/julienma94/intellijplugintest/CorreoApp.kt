package com.github.julienma94.intellijplugintest

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.core.concurrent.SimpleSuccessListener
import org.correomqtt.core.concurrent.SimpleTaskErrorResult
import org.correomqtt.core.concurrent.SimpleTaskErrorResultListener
import org.correomqtt.core.connection.ConnectionLifecycleTaskFactories
import org.correomqtt.core.model.MessageDTO
import org.correomqtt.core.model.MessageType
import org.correomqtt.core.model.Qos
import org.correomqtt.core.model.SubscriptionDTO
import org.correomqtt.core.pubsub.IncomingMessageEvent
import org.correomqtt.core.pubsub.PublishTaskFactory
import org.correomqtt.core.pubsub.SubscribeTaskFactory
import org.correomqtt.di.Inject
import org.correomqtt.di.Observes
import org.correomqtt.di.SingletonBean
import java.time.LocalDateTime
import java.util.*

@SingletonBean
class CorreoApp {

    private var correoCore: CorreoCore? = null
    private var connectionLifecycleTaskFactories: ConnectionLifecycleTaskFactories? = null
    private var publishTaskFactory: PublishTaskFactory? = null
    private var subscribeTaskFactory: SubscribeTaskFactory? = null

    @Inject
    constructor(
            correoCore: CorreoCore,
            connectionLifecycleTaskFactories: ConnectionLifecycleTaskFactories,
            publishTaskFactory: PublishTaskFactory,
            subscribeTaskFactory: SubscribeTaskFactory
    ) {
        this.correoCore = correoCore
        this.connectionLifecycleTaskFactories = connectionLifecycleTaskFactories
        this.publishTaskFactory = publishTaskFactory
        this.subscribeTaskFactory = subscribeTaskFactory
    }

    fun init() {
        thisLogger().info("Correo App init")
        correoCore!!.init()
        connectionLifecycleTaskFactories!!.connectFactory
                .create("b9640522-db21-11e9-a31c-57c858de472e")
                .onSuccess {
                    subscribe()
                }
                .run()
    }


    fun incomingMessageHook(@Observes event : IncomingMessageEvent ) {
        System.out.println(event.messageDTO.payload)
    }

    private fun subscribe() {
        val subscribeDTO = SubscriptionDTO.builder()
                .topic("/intellij/test1234")
                .qos(Qos.AT_LEAST_ONCE)
                .build()

        subscribeTaskFactory!!.create("b9640522-db21-11e9-a31c-57c858de472e",subscribeDTO)
                .onSuccess {
                    publish()
                }
                .onError {
                    System.out.println(it.unexpectedError.message)
                }
                .run()
    }

    private fun publish() {
        val messageDTO = MessageDTO.builder()
                .topic("/intellij/test1234")
                .payload("hello world")
                .qos(Qos.AT_LEAST_ONCE)
                .messageId(UUID.randomUUID().toString())
                .messageType(MessageType.OUTGOING)
                .dateTime(LocalDateTime.now())
                .build()
        publishTaskFactory!!.create("b9640522-db21-11e9-a31c-57c858de472e", messageDTO)
                .run()
    }
}