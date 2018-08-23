package ru.surfstudio.android.shared.pref.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.shared.pref.sample.app.dagger.AppComponent
import ru.surfstudio.android.shared.pref.sample.app.dagger.DaggerAppComponent

/**
 * Класс приложения
 */
class App : CoreApp() {

    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this))
                .build()
    }
}