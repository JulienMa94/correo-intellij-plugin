package com.github.julienma94.intellijplugintest.core.services.connection

import org.correomqtt.core.model.ConnectionConfigDTO

interface ConnectionManagerService {

    fun getConnections(): List<ConnectionConfigDTO>

    fun connect(connectionId: String)
    fun disconnect(connectionId: String)
}