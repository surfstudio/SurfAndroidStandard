package ru.surfstudio.android.filestorage.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppModule
import ru.surfstudio.android.filestorage.sample.app.dagger.DaggerCustomAppComponent

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
                .customAppModule(CustomAppModule(this))
                .build()
    }
}