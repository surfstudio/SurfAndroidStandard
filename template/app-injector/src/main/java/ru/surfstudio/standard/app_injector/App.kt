package ru.surfstudio.standard.app_injector

import android.app.Activity
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.notification.NotificationCenter
import ru.surfstudio.android.template.app_injector.BuildConfig
import ru.surfstudio.standard.app_injector.ui.notification.PushHandleStrategyFactory
import ru.surfstudio.standard.app_injector.ui.screen.configurator.storage.ScreenConfiguratorStorage
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import ru.surfstudio.standard.domain.notification.NotificationType

class App : CoreApp() {

    companion object {
        private const val debugNotificationTitle = "Debug screen"
        private const val debugNotificationBody = "Show debug info"
    }

    override fun onCreate() {
        super.onCreate()
        initFabric()
        initComponentProvider()
        initNotificationCenter()
        initDebugNotification()

        AppInjector.initInjector(this)
    }

    private fun initComponentProvider() {
        ComponentProvider.createActivityScreenConfigurator = { intent, kclass ->
            ScreenConfiguratorStorage.activityScreenConfiguratorMap[kclass]?.invoke(intent)!!
        }

        ComponentProvider.createActivityConfigurator = { intent, kclass ->
            ScreenConfiguratorStorage.activityConfiguratorMap[kclass]?.invoke(intent)!!
        }

        ComponentProvider.createFragmentScreenConfigurator = { bundle, kclass ->
            ScreenConfiguratorStorage.fragmentScreenConfiguratorMap[kclass]?.invoke(bundle)!!
        }

        ComponentProvider.createDialogScreenConfigurator = { bundle, kclass ->
            ScreenConfiguratorStorage.dialogScreenConfiguratorMap[kclass]?.invoke(bundle)!!
        }

        ComponentProvider.createWidgetScreenConfigurator = { kclass ->
            ScreenConfiguratorStorage.widgetScreenConfiguratorMap[kclass]?.invoke()!!
        }
    }

    private fun initFabric() {
        Fabric.with(this, *getFabricKits())
    }

    private fun getFabricKits() = arrayOf(Crashlytics.Builder()
            .core(CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build())
            .build())

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

    private fun initDebugNotification() {
        NotificationCenter.onReceiveMessage(
                this,
                debugNotificationTitle,
                debugNotificationBody,
                // todo use AbstractPushHandleStrategyFactory.key instead of "event"
                mapOf("event" to NotificationType.DEBUG_DATA_TYPE.getStringId())
        )
    }
}