package ru.surfstudio.standard.app

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.Kit
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.dagger.CoreAppModule
import ru.surfstudio.standard.app.dagger.AppComponent
import ru.surfstudio.standard.app.dagger.DaggerAppComponent

/**
 * Класс приложения
 */
class App : CoreApp() {

    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initFabric()
        initInjector()
    }

    private fun initFabric() {
        val kits = arrayOf<Kit<*>>(Crashlytics.Builder().core(CrashlyticsCore.Builder().build()).build())
        Fabric.with(this, *kits)
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .coreAppModule(CoreAppModule(this))
                .build()
    }
}