package ru.surfstudio.android.filestorage.sample.app

import android.app.Activity
import androidx.multidex.MultiDexApplication
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppModule
import ru.surfstudio.android.filestorage.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.sample.dagger.app.DefaultActivityLifecycleCallbacks

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
            .customAppModule(CustomAppModule(this))
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