package com.github.julienma94.intellijplugintest

import com.intellij.openapi.diagnostic.thisLogger
import org.correomqtt.core.CorreoCore
import org.correomqtt.di.Inject
import org.correomqtt.di.SingletonBean

@SingletonBean
class CorreoPlugin {
    private var correoCore: CorreoCore? = null

    private var isInitialized = false;

    @Inject
    constructor(
            correoCore: CorreoCore,
    ) {
        this.correoCore = correoCore
    }

    fun init() {
        if (!isInitialized) {
            thisLogger().info("Correo App init")
            correoCore!!.init()
            isInitialized = true;
        }
    }
}


// MVP roundtrip
//        connectionLifecycleTaskFactories!!.connectFactory
//                .create("b9640522-db21-11e9-a31c-57c858de472e")
//                .onSuccess {
//                    subscribe()
//                }
//                .run()


//    fun incomingMessageHook(@Observes event : IncomingMessageEvent ) {
//        System.out.println(event.messageDTO.payload)
//    }

//    private fun subscribe() {
//        val subscribeDTO = SubscriptionDTO.builder()
//                .topic("/intellij/test1234")
//                .qos(Qos.AT_LEAST_ONCE)
//                .build()
//
//        subscribeTaskFactory!!.create("b9640522-db21-11e9-a31c-57c858de472e",subscribeDTO)
//                .onSuccess {
//                    publish()
//                }
//                .onError {
//                    System.out.println(it.unexpectedError.message)
//                }
//                .run()
//    }

//    private fun publish() {
//        val messageDTO = MessageDTO.builder()
//                .topic("/intellij/test1234")
//                .payload("hello world")
//                .qos(Qos.AT_LEAST_ONCE)
//                .messageId(UUID.randomUUID().toString())
//                .messageType(MessageType.OUTGOING)
//                .dateTime(LocalDateTime.now())
//                .build()
//        publishTaskFactory!!.create("b9640522-db21-11e9-a31c-57c858de472e", messageDTO)
//                .run()
//    }