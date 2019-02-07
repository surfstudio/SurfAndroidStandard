package ru.surfstudio.standard.application.app

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.template.base_feature.BuildConfig
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.application.app.di.AppInjector
import ru.surfstudio.standard.f_debug.injector.DebugAppInjector

class App : CoreApp() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { Logger.e(it) }
        AppInjector.initInjector(this)
        DebugAppInjector.initInjector(this, activeActivityHolder)
        if (DebugAppInjector.debugInteractor.mustNotInitializeApp()) {
            // работает LeakCanary, ненужно ничего инициализировать
            return
        }
        initFabric()
        DebugAppInjector.debugInteractor.onCreateApp(R.mipmap.ic_launcher)
    }

    private fun initFabric() {
        Fabric.with(this, *getFabricKits())
    }

    private fun getFabricKits() = arrayOf(Crashlytics.Builder()
            .core(CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build())
            .build())
}