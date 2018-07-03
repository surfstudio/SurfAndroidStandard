package ru.surfstudio.standard.app_injector.ui.configurator

import android.content.Context
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.DaggerActivityComponent

class ActivityConfigurator(val context: Context) :
        BaseActivityConfigurator<ActivityComponent, ru.surfstudio.standard.app_injector.AppComponent>() {

    override fun createActivityComponent(parentComponent: ru.surfstudio.standard.app_injector.AppComponent?): ActivityComponent =
            DaggerActivityComponent.builder()
                    .appComponent(parentComponent)
                    .build()

    override fun getParentComponent(): ru.surfstudio.standard.app_injector.AppComponent =
            (context.applicationContext as ru.surfstudio.standard.app_injector.App).appComponent
}