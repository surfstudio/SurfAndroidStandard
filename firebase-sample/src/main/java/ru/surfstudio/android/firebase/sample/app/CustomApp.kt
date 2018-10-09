package ru.surfstudio.android.firebase.sample.app

import android.app.Activity
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.Kit
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.firebase.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.firebase.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    override fun onCreate() {
        super.onCreate()
        initFabric()
        initInjector()
        initNotificationCenter()
    }

    private fun initFabric() {
        val kits = arrayOf<Kit<*>>(
                Crashlytics.Builder().core(
                        CrashlyticsCore.Builder()
                                .build())
                        .build())
        Fabric.with(this, *kits)
    }

    private fun initInjector() {
        AppConfigurator.initInjector(this)
    }

    private fun initNotificationCenter() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                AppConfigurator.customAppComponent?.notificationManager()?.onActivityStarted(activity)
            }
        })
    }
}