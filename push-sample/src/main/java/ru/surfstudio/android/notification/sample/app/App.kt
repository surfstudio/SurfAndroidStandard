package ru.surfstudio.android.notification.sample.app

import android.app.Activity
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.DefaultActivityLifecycleCallbacks
import ru.surfstudio.android.notification.NotificationCenter
import ru.surfstudio.android.notification.sample.app.dagger.AppComponent
import ru.surfstudio.android.notification.sample.app.dagger.AppModule
import ru.surfstudio.android.notification.sample.app.dagger.DaggerAppComponent
import ru.surfstudio.android.notification.sample.ui.common.notification.PushHandleStrategyFactory

/**
 * Класс приложения
 */
class App : CoreApp() {

    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initInjector()
        initNotificationCenter()
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initNotificationCenter() {
        NotificationCenter.configure {
            setActiveActivityHolder(activeActivityHolder)
            setPushHandleStrategyFactory(PushHandleStrategyFactory)
        }

        registerActivityLifecycleCallbacks(object : DefaultActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                NotificationCenter.onActivityStarted(activity)
            }
        })
    }
}