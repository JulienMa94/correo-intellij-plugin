package com.github.julienma94.intellijplugintest

import org.correomqtt.di.TaskToFrontendPush

class PlatformRunLaterExecutor : TaskToFrontendPush {
    override fun pushToFrontend(runnable: Runnable) {
        runnable.run()
    }
}
