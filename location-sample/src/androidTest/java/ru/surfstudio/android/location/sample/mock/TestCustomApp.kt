package ru.surfstudio.android.location.sample.mock

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import ru.surfstudio.standard.base.app.ActiveActivityHolder
import ru.surfstudio.android.location.sample.app.CustomApp
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

class TestCustomApp : CustomApp() {

    override fun initInjector() {
        val app = ApplicationProvider.getApplicationContext<Context>().applicationContext as Application
        customAppComponent = DaggerTestCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(app, ru.surfstudio.standard.base.app.ActiveActivityHolder()))
                .build()
    }
}