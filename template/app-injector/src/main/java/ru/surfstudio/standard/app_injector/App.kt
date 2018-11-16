package ru.surfstudio.standard.app_injector

import android.app.Activity
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.template.app_injector.BuildConfig
import ru.surfstudio.standard.app_injector.ui.notification.debug.DebugNotificationBuilder
import ru.surfstudio.standard.app_injector.ui.screen.configurator.storage.ScreenConfiguratorStorage
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider

class App : CoreApp() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { Logger.e(it) }
        AppInjector.initInjector(this)

        initFabric()
        initComponentProvider()
        DebugNotificationBuilder.showDebugNotification(this)

        initNotificationHandler()
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

    private fun initNotificationHandler() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                AppInjector.appComponent.pushHandler().onActivityStarted(activity)
            }
        })
    }
}