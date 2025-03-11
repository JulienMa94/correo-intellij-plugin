package com.correomqtt.intellij.core.services.connection

import org.correomqtt.core.model.ConnectionConfigDTO

interface ConnectionManagerService {

    fun getConnections(): List<ConnectionConfigDTO>
    fun connect(connectionData: ConnectionConfigDTO, index: Int)
    fun disconnect()
    fun setActiveConnectionId(connectionId: String)
    fun getActiveConnectionId(): String
    fun switch(connectionData: ConnectionConfigDTO): Boolean
}