package ru.surfstudio.standard.app_injector

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