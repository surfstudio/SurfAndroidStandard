package ru.surfstudio.android.security.sample.app

import android.app.Activity
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.security.sample.app.dagger.CustomAppModule
import ru.surfstudio.android.security.sample.app.dagger.DaggerCustomAppComponent

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null

    private val sessionManager by lazy { customAppComponent?.sessionManager() }

    override fun onCreate() {
        super.onCreate()
        initInjector()
        registerSessionManager()
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
                .customAppModule(CustomAppModule(this))
                .build()
    }

    private fun registerSessionManager() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityStarted(activity: Activity) {
                sessionManager?.onActivityStarted(activity)
            }

            override fun onActivityStopped(activity: Activity) {
                sessionManager?.onActivityStopped()
            }
        })
    }
}