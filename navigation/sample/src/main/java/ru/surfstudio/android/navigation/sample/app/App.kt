package ru.surfstudio.android.navigation.sample.app

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.di.supplier.callbacks.ActivityNavigationSupplierCallbacks
import ru.surfstudio.android.navigation.di.supplier.callbacks.FragmentNavigationSupplierCallbacks
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.executor.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.fragment.FragmentCommandExecutor

class App : Application() {

    companion object {
        lateinit var navigator: AppCommandExecutor
    }

    override fun onCreate() {
        super.onCreate()
        initExecutor()
    }

    private fun initExecutor() {
        val fragmentCallbacksCreator = ::createFragmentNavigationSupplierCallbacks
        val activityCallbacks = ActivityNavigationSupplierCallbacks(fragmentCallbacksCreator)
        registerActivityLifecycleCallbacks(activityCallbacks)
        navigator = AppCommandExecutor(
                ActivityCommandExecutor(activityCallbacks),
                FragmentCommandExecutor(activityCallbacks),
                DialogCommandExecutor(activityCallbacks)
        )
    }

    private fun createFragmentNavigationSupplierCallbacks(
            activity: AppCompatActivity,
            savedState: Bundle?
    ): FragmentNavigationSupplierCallbacks {
        return FragmentNavigationSupplierCallbacks(activity, savedState)
    }
}