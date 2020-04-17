package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.sample.app.utils.animations.SlideAnimations

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutor
        lateinit var provider: ActivityNavigationProvider
    }

    override fun onCreate() {
        super.onCreate()
        initExecutor()
        initAnimations()
    }

    private fun initExecutor() {
        val callbacksSupplier = ActivityNavigationProviderCallbacks()
        registerActivityLifecycleCallbacks(callbacksSupplier)
        navigator = AppCommandExecutor(callbacksSupplier)
        provider = callbacksSupplier
    }

    private fun initAnimations() {
        DefaultAnimations.fragment = SlideAnimations()
    }
}