package com.correomqtt.plugin.core.services.publish

import org.correomqtt.core.model.Qos

interface PublishService {
    fun publish(topic: String, payload: String, qos: Qos, retained: Boolean)
}
