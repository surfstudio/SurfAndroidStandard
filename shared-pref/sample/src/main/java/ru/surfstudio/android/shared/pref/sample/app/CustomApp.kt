package ru.surfstudio.android.shared.pref.sample.app

import android.app.Activity
import androidx.multidex.MultiDexApplication
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.sample.dagger.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.shared.pref.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.shared.pref.sample.app.dagger.DaggerCustomAppComponent

/**
 * Класс приложения
 */
class CustomApp : MultiDexApplication() {

    val activeActivityHolder = ActiveActivityHolder()

    var customAppComponent: CustomAppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
        registerActiveActivityListener()
    }

    private fun initInjector() {
        customAppComponent = DaggerCustomAppComponent.builder()
            .defaultAppModule(DefaultAppModule(this, activeActivityHolder))
            .build()
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(customAppComponent?.navigationProviderCallbacks())
        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                activeActivityHolder.activity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                activeActivityHolder.clearActivity()
            }
        })
    }
}