package ru.surfstudio.standard.app_injector

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDexApplication
//import com.crashlytics.android.Crashlytics
//import com.crashlytics.android.core.CrashlyticsCore
import com.github.anrwatchdog.ANRWatchDog
//import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.logger.RemoteLogger
import ru.surfstudio.android.logger.logging_strategies.impl.remote_logger.RemoteLoggerLoggingStrategy
import ru.surfstudio.android.logger.logging_strategies.impl.timber.TimberLoggingStrategy
import ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics.CrashlyticsRemoteLoggingStrategy
import ru.surfstudio.android.notification.interactor.PushClickProvider
import ru.surfstudio.android.notification.interactor.PushEventListener
import ru.surfstudio.android.template.app_injector.BuildConfig
import ru.surfstudio.android.template.app_injector.R
import ru.surfstudio.standard.app_injector.ui.navigation.RouteClassStorage
import ru.surfstudio.standard.app_injector.ui.screen.configurator.storage.ScreenConfiguratorStorage
import ru.surfstudio.standard.base_ui.DefaultActivityLifecycleCallbacks
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import ru.surfstudio.standard.base_ui.provider.route.RouteClassProvider
import ru.surfstudio.standard.f_debug.injector.DebugAppInjector

class App : MultiDexApplication() {

    val activeActivityHolder = ActiveActivityHolder()

    override fun onCreate() {
        super.onCreate()

        initAnrWatchDog()
        initLog()
        registerActiveActivityListener()

        RxJavaPlugins.setErrorHandler { Logger.e(it) }
        AppInjector.initInjector(this)
        DebugAppInjector.initInjector(this, activeActivityHolder)
        if (DebugAppInjector.debugInteractor.mustNotInitializeApp()) {
            // работает LeakCanary, ненужно ничего инициализировать
            return
        }

//        initFabric()
        initComponentProvider()
        initRouteProvider()
        initPushEventListener()
        DebugAppInjector.debugInteractor.onCreateApp(R.mipmap.ic_launcher)
    }

    private fun initRouteProvider() {
        RouteClassProvider.getActivityClass = { kclass -> RouteClassStorage.activityRouteMap[kclass]!! }
        RouteClassProvider.getFragmentClass = { kclass -> RouteClassStorage.fragmentRouteMap[kclass]!! }
        RouteClassProvider.getDialogClass = { kclass -> RouteClassStorage.dialogRouteMap[kclass]!! }
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

//    private fun initFabric() {
//        Fabric.with(this, *getFabricKits())
//    }

//    private fun getFabricKits() = arrayOf(Crashlytics.Builder()
//            .core(CrashlyticsCore.Builder()
//                    .disabled(BuildConfig.DEBUG)
//                    .build())
//            .build())

    /**
     * отслеживает ANR и отправляет в крашлитикс
     */
    private fun initAnrWatchDog() {
        ANRWatchDog().setReportMainThreadOnly()
                .setANRListener{ RemoteLogger.logError(it) }
                .start()
    }

    private fun initLog() {
        Logger.addLoggingStrategy(TimberLoggingStrategy())
        Logger.addLoggingStrategy(RemoteLoggerLoggingStrategy())
        RemoteLogger.addRemoteLoggingStrategy(CrashlyticsRemoteLoggingStrategy())
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                activeActivityHolder.activity = activity
            }

            override fun onActivityStopped(activity: Activity) {
                activeActivityHolder.clearActivity()
            }
        })
    }

    private fun initPushEventListener() {
        PushClickProvider.pushEventListener = object: PushEventListener {
            override fun pushDismissListener(context: Context, intent: Intent) {
                //todo
            }

            override fun pushOpenListener(context: Context, intent: Intent) {
                //todo
            }
        }
    }
}