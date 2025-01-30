package com.github.julienma94.intellijplugintest.core.services.startup

interface StartUpService {
    fun init(configDirectoryBasePath: String = "")
    fun getBaseDirectoryPath(): String?
}
