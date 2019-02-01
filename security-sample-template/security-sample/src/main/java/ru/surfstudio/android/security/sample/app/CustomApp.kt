package ru.surfstudio.android.security.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.security.app.AppDebuggableChecker
import ru.surfstudio.android.security.sample.BuildConfig
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.security.sample.app.dagger.DaggerCustomAppComponent

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null
    private val sessionActivityCallback by lazy { customAppComponent?.sessionActivityCallback() }

    override fun onCreate() {
        super.onCreate()
        AppDebuggableChecker.check(this, BuildConfig.CHECK_DEBUGGABLE)
        initInjector()
        registerSessionManager()
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this, activeActivityHolder))
                .build()
    }

    private fun registerSessionManager() {
        registerActivityLifecycleCallbacks(sessionActivityCallback)
    }
}