package ru.surfstudio.android.security.app

import android.content.Context
import android.content.pm.ApplicationInfo
import ru.surfstudio.android.security.BuildConfig

object AppDebuggableChecker {
    fun check(context: Context, checkDebuggable: Boolean) {
        if (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0 && checkDebuggable) {
            throw RuntimeException().apply {
                //throw exception without stacktrace
                stackTrace = arrayOfNulls(0)
            }
        }
    }
}