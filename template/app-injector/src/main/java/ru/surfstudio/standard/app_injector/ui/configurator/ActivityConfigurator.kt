package ru.surfstudio.standard.app_injector.ui.configurator

import android.content.Context
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator
import ru.surfstudio.standard.app_injector.AppComponent
import ru.surfstudio.standard.app_injector.AppInjector
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.DaggerActivityComponent

class ActivityConfigurator(
        val context: Context
) : BaseActivityConfigurator<ActivityComponent, AppComponent>() {

    override fun createActivityComponent(parentComponent: AppComponent?): ActivityComponent =
            DaggerActivityComponent.builder()
                    .appComponent(parentComponent)
                    .build()

    override fun getParentComponent(): ru.surfstudio.standard.app_injector.AppComponent = AppInjector.appComponent
}