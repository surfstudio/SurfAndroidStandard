package ru.surfstudio.standard.app_injector.ui.configurator

import android.app.Activity
import android.content.Intent
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.ActivityModule
import ru.surfstudio.standard.app_injector.ui.DaggerActivityComponent
import ru.surfstudio.standard.app_injector.ui.screen.ActivityScreenModule

/**
 * Базовый конфигуратор для экрана, основанного на [Activity]
 */
abstract class ActivityScreenConfigurator(intent: Intent) :
        BaseActivityViewConfigurator<ru.surfstudio.standard.app_injector.AppComponent, ActivityComponent, ActivityScreenModule>(intent) {

    override fun createActivityComponent(parentComponent: ru.surfstudio.standard.app_injector.AppComponent): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .activityModule(ActivityModule(persistentScope))
                .build()
    }

    override fun getParentComponent(): ru.surfstudio.standard.app_injector.AppComponent {
        return (getTargetActivity<CoreActivityView>().applicationContext as ru.surfstudio.standard.app_injector.App).appComponent
    }

    override fun getActivityScreenModule(): ActivityScreenModule {
        return ActivityScreenModule(persistentScope)
    }
}