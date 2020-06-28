package ru.surfstudio.android.navigation.sample_standard

import android.app.Activity
import android.app.Application
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.sample_standard.di.AppComponent
import ru.surfstudio.android.navigation.sample_standard.di.AppNavigationModule
import ru.surfstudio.android.navigation.sample_standard.di.DaggerAppComponent
import ru.surfstudio.android.navigation.sample_standard.utils.animations.FadeAnimations
import ru.surfstudio.android.navigation.sample_standard.utils.animations.SlideAnimations
import ru.surfstudio.android.sample.dagger.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

class App : Application() {

    lateinit var appComponent: AppComponent
    private val activeActivityHolder = ActiveActivityHolder()

    override fun onCreate() {
        super.onCreate()
        initInjector()
        registerActiveActivityListener()
        registerNavigationProviderCallbacks()
        initAnimations()
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .defaultAppModule(DefaultAppModule(this, activeActivityHolder))
                .appNavigationModule(AppNavigationModule())
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

    private fun registerNavigationProviderCallbacks() {
        val provider = appComponent.activityNavigationProvider()
        val callbackProvider = provider as? ActivityNavigationProviderCallbacks ?: return
        registerActivityLifecycleCallbacks(callbackProvider)
    }

    private fun initAnimations() {
        DefaultAnimations.fragment = SlideAnimations() //animations for all fragment changes
        DefaultAnimations.tab = FadeAnimations() //animations for tab changes
    }
}