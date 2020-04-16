package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import ru.surfstudio.android.navigation.di.supplier.callbacks.ActivityNavigationSupplierCallbacks
import ru.surfstudio.android.navigation.executor.AppCommandExecutor

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutor
    }

    override fun onCreate() {
        super.onCreate()
        initExecutor()
    }

    private fun initExecutor() {
        val activityCallbacks = ActivityNavigationSupplierCallbacks()
        registerActivityLifecycleCallbacks(activityCallbacks)
        navigator = AppCommandExecutor(activityCallbacks)
    }
}