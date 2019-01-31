package ru.surfstudio.android.custom_scope_sample.app

import android.app.Activity
import androidx.multidex.MultiDexApplication
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.standard.app_injector.ui.DefaultActivityLifecycleCallbacks

/**
 * Класс приложения
 */
class App : MultiDexApplication() {

    val activeActivityHolder = ActiveActivityHolder()

    override fun onCreate() {
        super.onCreate()
        initInjector()
        registerActiveActivityListener()
    }

    private fun initInjector() {
        AppConfigurator.initInjector(this)
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