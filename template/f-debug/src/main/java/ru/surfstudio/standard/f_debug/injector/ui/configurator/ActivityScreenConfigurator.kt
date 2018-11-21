package ru.surfstudio.standard.f_debug.injector.ui.configurator

import android.app.Activity
import android.content.Intent
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.standard.f_debug.injector.DebugAppComponent
import ru.surfstudio.standard.f_debug.injector.DebugAppInjector
import ru.surfstudio.standard.f_debug.injector.ui.ActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.ActivityModule
import ru.surfstudio.standard.f_debug.injector.ui.DaggerActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.screen.ActivityScreenModule

/**
 * Базовый конфигуратор для экрана, основанного на [Activity]
 */
abstract class ActivityScreenConfigurator(
        intent: Intent
) : BaseActivityViewConfigurator<DebugAppComponent, ActivityComponent, ActivityScreenModule>(intent) {

    override fun createActivityComponent(parentComponent: DebugAppComponent): ActivityComponent {
        return DaggerActivityComponent.builder()
                .debugAppComponent(parentComponent)
                .activityModule(ActivityModule(persistentScope))
                .build()
    }

    override fun getParentComponent(): DebugAppComponent {
        return DebugAppInjector.appComponent
    }

    override fun getActivityScreenModule(): ActivityScreenModule {
        return ActivityScreenModule(persistentScope)
    }
}