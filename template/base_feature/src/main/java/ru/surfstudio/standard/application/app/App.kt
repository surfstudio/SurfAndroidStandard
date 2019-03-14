package ru.surfstudio.standard.application.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.github.anrwatchdog.ANRWatchDog
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.logger.RemoteLogger
import ru.surfstudio.android.logger.logging_strategies.impl.remote_logger.RemoteLoggerLoggingStrategy
import ru.surfstudio.android.logger.logging_strategies.impl.timber.TimberLoggingStrategy
import ru.surfstudio.android.logger.remote_logging_strategies.impl.crashlytics.CrashlyticsRemoteLoggingStrategy
import ru.surfstudio.android.notification.ui.PushClickProvider
import ru.surfstudio.android.notification.ui.PushEventListener
import ru.surfstudio.android.template.base_feature.BuildConfig
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.application.app.di.AppInjector
import ru.surfstudio.standard.ui.activity.DefaultActivityLifecycleCallbacks
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

        initFabric()
        initPushEventListener()
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