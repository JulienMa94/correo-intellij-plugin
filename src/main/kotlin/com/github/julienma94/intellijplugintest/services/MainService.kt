package com.github.julienma94.intellijplugintest.services

import com.github.julienma94.intellijplugintest.CorreoApp
import com.github.julienma94.intellijplugintest.MyBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import org.correomqtt.di.SoyDi

interface MainService {
    fun init()
}
