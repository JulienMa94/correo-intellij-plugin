package com.github.julienma94.intellijplugintest.core.services.secruity

interface KeyringManagerService {

    fun migrate(newKeyringIdentifier: String)
    fun getMasterPassword(): String
    fun init()
    fun wipe()
}