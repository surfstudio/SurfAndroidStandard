package ru.surfstudio.android.security.sample.app

import android.app.Activity
import android.os.Bundle
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.logger.Logger.d
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.security.app.AppDebuggableChecker
import ru.surfstudio.android.security.sample.BuildConfig
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.security.sample.app.dagger.DaggerCustomAppComponent
import java.util.logging.Logger

/**
 * Класс приложения
 */
class CustomApp : CoreApp() {

    var customAppComponent: CustomAppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
        AppDebuggableChecker.check(this, BuildConfig.CHECK_DEBUGGABLE)
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this, activeActivityHolder))
                .build()
    }

    override fun registerActiveActivityListener() {
        super.registerActiveActivityListener()
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityStarted(activity: Activity) {
                d("Started: ${activity.javaClass.simpleName}")
            }

            override fun onActivityStopped(activity: Activity) {
                super.onActivityStopped(activity)
                d("Stopped: ${activity.javaClass.simpleName}")
            }

            /*override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                super.onActivityCreated(activity, bundle)
                d("Created: ${activity.javaClass.simpleName}")
            }

            override fun onActivityResumed(activity: Activity) {
                super.onActivityResumed(activity)
                d("Resumed: ${activity.javaClass.simpleName}")
            }

            override fun onActivityPaused(activity: Activity) {
                super.onActivityPaused(activity)
                d("Paused: ${activity.javaClass.simpleName}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
                super.onActivitySaveInstanceState(activity, bundle)
                d("SaveInstanceState: ${activity.javaClass.simpleName}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                super.onActivityDestroyed(activity)
                d("Destroyed: ${activity.javaClass.simpleName}")
            }*/
        })
    }
}