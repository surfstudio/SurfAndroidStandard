package ru.surfstudio.android.filestorage.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.filestorage.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this))
                .build()
    }
}