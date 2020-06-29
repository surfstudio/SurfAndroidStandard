package ru.surfstudio.android.push.sample.app

import ru.surfstudio.android.push.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.push.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

object AppConfigurator {

    var customAppComponent: CustomAppComponent? = null

    fun initInjector(app: CustomApp) {
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(app, app.activeActivityHolder))
                .build()
    }
}