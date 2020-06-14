package ru.surfstudio.android.navigation.sample_standard.dagger.ui.configurator

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator
import ru.surfstudio.android.navigation.sample_standard.App
import ru.surfstudio.android.navigation.sample_standard.dagger.AppComponent
import ru.surfstudio.android.navigation.sample_standard.dagger.AppNavigationModule
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.ActivityNavigationModule
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.DaggerActivityComponent
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
