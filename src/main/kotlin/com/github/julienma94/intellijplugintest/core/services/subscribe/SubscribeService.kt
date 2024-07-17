package com.github.julienma94.intellijplugintest.core.services.subscribe

import org.correomqtt.core.model.Qos

interface SubscribeService {
    fun subscribe(topic: String, qos: Qos)
}
