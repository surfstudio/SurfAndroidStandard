package ru.surfstudio.android.navigation.sample_standard.di.ui.configurator

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator
import ru.surfstudio.android.navigation.sample_standard.App
import ru.surfstudio.android.navigation.sample_standard.di.AppComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityNavigationModule
import ru.surfstudio.android.navigation.sample_standard.di.ui.DaggerActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

class ActivityConfigurator : BaseActivityConfigurator<ActivityComponent, AppComponent>() {

    @Override
    override fun createActivityComponent(parentComponent: AppComponent): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .defaultActivityModule((DefaultActivityModule(persistentScope)))
                .activityNavigationModule(ActivityNavigationModule())
                .build()
    }

    @Override
    override fun getParentComponent(): AppComponent {
        val app = persistentScope.screenState.activity.applicationContext as App
        return app.appComponent
    }
}
