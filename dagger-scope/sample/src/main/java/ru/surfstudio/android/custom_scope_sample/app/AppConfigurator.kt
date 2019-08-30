package ru.surfstudio.android.custom_scope_sample.app

import ru.surfstudio.android.custom_scope_sample.app.dagger.AppComponent
import ru.surfstudio.android.custom_scope_sample.app.dagger.AppModule
import ru.surfstudio.android.custom_scope_sample.app.dagger.DaggerAppComponent

object AppConfigurator {

    var appComponent: AppComponent? = null

    fun initInjector(app: App) {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
    }
}