package ru.surfstudio.standard.f_debug.injector.ui.configurator

import android.app.Activity
import android.content.Intent
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.standard.f_debug.injector.DebugAppComponent
import ru.surfstudio.standard.f_debug.injector.DebugAppInjector
import ru.surfstudio.standard.f_debug.injector.ui.DaggerDebugActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.DebugActivityComponent
import ru.surfstudio.standard.f_debug.injector.ui.DebugActivityModule
import ru.surfstudio.standard.f_debug.injector.ui.screen.DebugActivityScreenModule

/**
 * Базовый конфигуратор для экрана, основанного на [Activity]
 */
abstract class DebugActivityScreenConfigurator(
        intent: Intent
) : BaseActivityViewConfigurator<DebugAppComponent, DebugActivityComponent, DebugActivityScreenModule>(intent) {

    override fun createActivityComponent(parentComponent: DebugAppComponent): DebugActivityComponent {
        return DaggerDebugActivityComponent.builder()
                .debugAppComponent(parentComponent)
                .debugActivityModule(DebugActivityModule(persistentScope))
                .build()
    }

    override fun getParentComponent(): DebugAppComponent {
        return DebugAppInjector.appComponent
    }

    override fun getActivityScreenModule(): DebugActivityScreenModule {
        return DebugActivityScreenModule(persistentScope)
    }
}