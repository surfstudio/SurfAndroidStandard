package ru.surfstudio.standard.f_debug.injector

import android.app.Application
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.standard.f_debug.DebugInteractor

object DebugAppInjector {

    lateinit var debugInteractor: DebugInteractor

    fun initInjector(app: Application, activeActivityHolder: ActiveActivityHolder) {
        debugInteractor = DebugInteractor()
    }
}