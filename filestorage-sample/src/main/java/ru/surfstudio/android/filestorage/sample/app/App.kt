package ru.surfstudio.android.filestorage.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.filestorage.sample.app.dagger.AppComponent
import ru.surfstudio.android.filestorage.sample.app.dagger.AppModule
import ru.surfstudio.android.filestorage.sample.app.dagger.DaggerAppComponent

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
                .appModule(AppModule(this))
                .build()
    }
}