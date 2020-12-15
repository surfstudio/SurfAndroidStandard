package ru.surfstudio.android.push.sample.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDexApplication
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.ui.PushClickProvider
import ru.surfstudio.android.notification.ui.PushEventListener
import ru.surfstudio.android.sample.dagger.app.DefaultActivityLifecycleCallbacks

/**
 * Класс приложения
 */
class CustomApp : MultiDexApplication() {

    val activeActivityHolder = ActiveActivityHolder()

    override fun onCreate() {
        super.onCreate()
        initInjector()
        initNotificationCenter()
        registerActiveActivityListener()
        initPushEventListener()
    }

    private fun initInjector() {
        AppConfigurator.initInjector(this)
    }

    private fun initNotificationCenter() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                AppConfigurator.customAppComponent?.pushHandler()?.onActivityStarted(activity)
            }
        })
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                activeActivityHolder.activity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                activeActivityHolder.clearActivity()
            }
        })
    }

    private fun initPushEventListener() {
        PushClickProvider.pushEventListener = object : PushEventListener {
            override fun pushDismissListener(context: Context, intent: Intent) {
                Logger.i("Push notification dismissed")
            }

            override fun pushOpenListener(context: Context, intent: Intent) {
                Logger.i("Push notification opened")
            }

            override fun customActionListener(context: Context, intent: Intent) {
                Logger.i("Push notification custom action")
            }
        }
    }
}