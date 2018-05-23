package ru.surfstudio.android.rx.downloader.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.rx.downloader.sample.app.dagger.AppComponent
import ru.surfstudio.android.rx.downloader.sample.app.dagger.AppModule
import ru.surfstudio.android.rx.downloader.sample.app.dagger.DaggerAppComponent

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