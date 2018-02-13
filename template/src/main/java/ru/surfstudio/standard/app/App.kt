package ru.surfstudio.standard.app

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.standard.BuildConfig
import ru.surfstudio.standard.app.dagger.ActiveActivityHolderModule
import ru.surfstudio.standard.app.dagger.AppComponent
import ru.surfstudio.standard.app.dagger.AppModule
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
        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val kit = Crashlytics.Builder().core(core).build()
        Fabric.with(this, kit)
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .activeActivityHolderModule(ActiveActivityHolderModule(activeActivityHolder))
                .build()
    }
}