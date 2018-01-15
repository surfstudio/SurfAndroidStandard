package ru.surfstudio.standard.ui.base.configurator

import android.content.Context

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator
import ru.surfstudio.standard.app.App
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.app.dagger.AppComponent
import ru.surfstudio.standard.app.dagger.DaggerActivityComponent


class ActivityConfigurator(private val context: Context) : BaseActivityConfigurator<ActivityComponent, AppComponent>() {

    override fun createActivityComponent(parentComponent: AppComponent): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .build()
    }

    override fun getParentComponent(): AppComponent? {
        return (context.applicationContext as App).appComponent
    }
}
