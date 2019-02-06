package ru.surfstudio.standard.application.app.di

import ru.surfstudio.standard.application.app.App

/**
 * Объект ответственный за создание и хранение [AppComponent]
 */
object AppInjector {

    lateinit var appComponent: AppComponent

    fun initInjector(app: App) {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(app, app.activeActivityHolder))
                .build()
    }
}