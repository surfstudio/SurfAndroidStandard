package ru.surfstudio.standard.f_debug.injector

import android.app.Application
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.standard.f_debug.DebugInteractor

/**
 * Объект ответственный за создание и хранение [DebugAppComponent]
 */
object DebugAppInjector {

    lateinit var appComponent: DebugAppComponent
    lateinit var debugInteractor: DebugInteractor

    fun initInjector(app: Application, activeActivityHolder: ActiveActivityHolder) {
        appComponent = DaggerDebugAppComponent.builder()
                .debugAppModule(DebugAppModule(app, activeActivityHolder))
                .build()
        debugInteractor = appComponent.debugInteractor()
    }
}