package ru.surfstudio.android.sample.dagger.app

import android.app.Activity
import android.app.Application
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.sample.dagger.app.dagger.DaggerDefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

class DefaultApp : Application() {

    val activeActivityHolder = ActiveActivityHolder()

    var defaultAppComponent: DefaultAppComponent? = null

    override fun onCreate() {
        super.onCreate()

        initInjector()
        registerActiveActivityListener()
    }

    private fun initInjector() {
        defaultAppComponent = DaggerDefaultAppComponent.builder()
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