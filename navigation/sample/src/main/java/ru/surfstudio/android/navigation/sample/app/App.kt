package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.executor.AppCommandExecutor

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutor
        lateinit var provider: ActivityNavigationProvider
    }

    override fun onCreate() {
        super.onCreate()
        initExecutor()
    }

    private fun initExecutor() {
        val callbacksSupplier = ActivityNavigationProviderCallbacks()
        registerActivityLifecycleCallbacks(callbacksSupplier)
        navigator = AppCommandExecutor(callbacksSupplier)
        provider = callbacksSupplier
    }
}