package ru.surfstudio.android.easyadapter.sample.app

import android.app.Activity
import androidx.multidex.MultiDexApplication
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.easyadapter.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.easyadapter.sample.app.dagger.DaggerCustomAppComponent
import ru.surfstudio.android.sample.dagger.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

class CustomApp : MultiDexApplication() {

    // bruh

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
}