package ru.surfstudio.android.app.migration.sample.app

import ru.surfstudio.android.app.migration.sample.app.dagger.AppComponent
import ru.surfstudio.android.app.migration.sample.app.dagger.DaggerAppComponent
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

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