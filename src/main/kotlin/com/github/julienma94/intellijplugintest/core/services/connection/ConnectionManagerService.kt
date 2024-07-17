package com.github.julienma94.intellijplugintest.core.services.connection

import org.correomqtt.core.model.ConnectionConfigDTO

interface ConnectionManagerService {

    fun getConnections(): List<ConnectionConfigDTO>
    fun connect(connectionId: String)
    fun disconnect(tabIndex: Int)
    fun addConnectionId(tabIndex: Int, connectionId: String)
    fun removeConnectionId(tabIndex: Int)
    fun setActiveConnectionId(connectionId: String)
    fun getActiveConnectionId(): String
}