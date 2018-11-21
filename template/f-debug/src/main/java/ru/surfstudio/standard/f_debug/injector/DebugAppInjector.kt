package ru.surfstudio.standard.f_debug.injector

import android.app.Application
import ru.surfstudio.android.core.app.ActiveActivityHolder

/**
 * Объект ответственный за создание и хранение [DebugAppComponent]
 */
object DebugAppInjector {

    lateinit var appComponent: DebugAppComponent

    fun initInjector(app: Application, activeActivityHolder: ActiveActivityHolder) {
        appComponent = DaggerDebugAppComponent.builder()
                .debugAppModule(DebugAppModule(app, activeActivityHolder))
                .build()
    }
}