package ru.surfstudio.android.firebase.sample.app

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.standard.app_injector.ui.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.firebase.sample.BuildConfig

/**
 * Класс приложения
 */
class CustomApp : MultiDexApplication() {

    val activeActivityHolder = ActiveActivityHolder()

    override fun onCreate() {
        super.onCreate()
        initFabric()
        initInjector()
        initNotificationCenter()
        registerActiveActivityListener()
    }

    private fun initFabric() {
        Fabric.with(this, *getFabricKits())
    }

    private fun getFabricKits() = arrayOf(Crashlytics.Builder()
            .core(CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build())
            .build())

    private fun initInjector() {
        AppConfigurator.initInjector(this)
    }

    private fun initNotificationCenter() {
        registerActivityLifecycleCallbacks(object: ru.surfstudio.standard.app_injector.ui.DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                AppConfigurator.customAppComponent?.pushHandler()?.onActivityStarted(activity)
            }
        })
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(object : ru.surfstudio.standard.app_injector.ui.DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                activeActivityHolder.activity = activity
            }

            override fun onActivityStopped(activity: Activity) {
                activeActivityHolder.clearActivity()
            }
        })
    }
}