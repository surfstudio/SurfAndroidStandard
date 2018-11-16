package ru.surfstudio.android.location.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.location.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.location.sample.app.dagger.DaggerCustomAppComponent

/**
 * Класс приложения
 */
open class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    protected open fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this, activeActivityHolder))
                .build()
    }
}