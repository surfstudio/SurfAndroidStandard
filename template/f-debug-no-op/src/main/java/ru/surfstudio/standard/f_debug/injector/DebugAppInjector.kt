package ru.surfstudio.standard.f_debug.injector

import android.app.Application
import ru.surfstudio.android.core.app.ActiveActivityHolder

object DebugAppInjector {

    lateinit var appComponent: DebugAppComponent

    fun initInjector(app: Application, activeActivityHolder: ActiveActivityHolder) {
        appComponent = DebugAppComponent()
    }
}