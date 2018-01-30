package ru.surfstudio.standard.app

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.Kit
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.standard.app.dagger.ActiveActivityHolderModule
import ru.surfstudio.standard.app.dagger.AppComponent
import ru.surfstudio.standard.app.dagger.AppModule

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
                .appModule(AppModule(this))
                .activeActivityHolderModule(ActiveActivityHolderModule(activeActivityHolder))
                .build()
    }
}