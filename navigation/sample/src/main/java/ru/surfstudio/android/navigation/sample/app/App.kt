package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import ru.surfstudio.android.navigation.supplier.ActivityNavigationSupplier
import ru.surfstudio.android.navigation.supplier.callbacks.ActivityNavigationSupplierCallbacks
import ru.surfstudio.android.navigation.executor.AppCommandExecutor

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutor
        lateinit var supplier: ActivityNavigationSupplier
    }

    override fun onCreate() {
        super.onCreate()
        initExecutor()
    }

    private fun initExecutor() {
        val callbacksSupplier = ActivityNavigationSupplierCallbacks()
        registerActivityLifecycleCallbacks(callbacksSupplier)
        navigator = AppCommandExecutor(callbacksSupplier)
        supplier = callbacksSupplier
    }
}