package ru.surfstudio.android.security.sample.app

import androidx.multidex.MultiDexApplication
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.security.app.ReleaseAppChecker
import ru.surfstudio.android.security.sample.BuildConfig
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.security.sample.app.dagger.DaggerCustomAppComponent

/**
 * Класс приложения
 */
class CustomApp : MultiDexApplication() {

    val activeActivityHolder = ActiveActivityHolder()
    var customAppComponent: CustomAppComponent? = null

    //листенер для SessionManager
    private val sessionActivityCallback by lazy { customAppComponent?.sessionActivityCallback() }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.CHECK_DEBUGGABLE) {
            ReleaseAppChecker.checkReleaseApp(this)
        }
        initInjector()
        registerSessionManager()
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
            .defaultAppModule(DefaultAppModule(this, activeActivityHolder))
            .build()
    }

    private fun registerSessionManager() {
        registerActivityLifecycleCallbacks(customAppComponent?.navigationProviderCallbacks())
        registerActivityLifecycleCallbacks(sessionActivityCallback)
    }
}