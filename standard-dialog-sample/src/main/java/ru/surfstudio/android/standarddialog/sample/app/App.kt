package ru.surfstudio.android.standarddialog.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.standarddialog.sample.app.dagger.AppComponent
import ru.surfstudio.android.standarddialog.sample.app.dagger.AppModule
import ru.surfstudio.android.standarddialog.sample.app.dagger.DaggerAppComponent

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