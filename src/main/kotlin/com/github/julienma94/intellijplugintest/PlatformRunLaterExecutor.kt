package com.github.julienma94.intellijplugintest

import com.intellij.util.ui.UIUtil
import org.correomqtt.di.TaskToFrontendPush

class PlatformRunLaterExecutor : TaskToFrontendPush {
    override fun pushToFrontend(runnable: Runnable) {
        UIUtil.invokeLaterIfNeeded(runnable)
    }
}
