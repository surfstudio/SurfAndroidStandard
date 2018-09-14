package ru.surfstudio.android.security.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.security.app.AppDebuggableChecker
import ru.surfstudio.android.security.sample.BuildConfig
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.security.sample.app.dagger.CustomAppModule
import ru.surfstudio.android.security.sample.app.dagger.DaggerCustomAppComponent

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
        AppDebuggableChecker.check(this, BuildConfig.CHECK_DEBUGGABLE)
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
                .customAppModule(CustomAppModule(this))
                .build()
    }
}