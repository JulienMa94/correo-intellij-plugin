package com.github.julienma94.intellijplugintest.core.services.publish

import org.correomqtt.core.model.Qos

interface PublishService {
    fun publish(topic: String, payload: String, qos: Qos)
}
