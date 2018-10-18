package ru.surfstudio.android.firebase.sample.app

import android.app.Activity
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.firebase.sample.BuildConfig
import ru.surfstudio.android.firebase.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.firebase.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.firebase.sample.ui.common.notification.PushHandleStrategyFactory
import ru.surfstudio.android.notification.NotificationCenter
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initFabric()
        initInjector()
        initNotificationCenter()
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
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this))
                .build()
    }

    private fun initNotificationCenter() {
        NotificationCenter.configure {
            setActiveActivityHolder(activeActivityHolder)
            setPushHandleStrategyFactory(PushHandleStrategyFactory)
        }

        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                NotificationCenter.onActivityStarted(activity)
            }
        })
    }
}