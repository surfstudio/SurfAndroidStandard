package ru.surfstudio.android.firebase.sample.app

import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.firebase.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.firebase.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

object AppConfigurator {

    var customAppComponent: CustomAppComponent? = null

    fun initInjector(app: CoreApp) {
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(app, app.activeActivityHolder))
                .build()
    }
}