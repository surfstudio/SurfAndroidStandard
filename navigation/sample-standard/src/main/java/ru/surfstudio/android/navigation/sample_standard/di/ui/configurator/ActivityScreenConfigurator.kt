package ru.surfstudio.android.navigation.sample_standard.di.ui.configurator

import android.content.Intent
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.navigation.sample_standard.App
import ru.surfstudio.android.navigation.sample_standard.di.AppComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityNavigationModule
import ru.surfstudio.android.navigation.sample_standard.di.ui.DaggerActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

abstract class ActivityScreenConfigurator(intent: Intent?) : BaseActivityViewConfigurator<AppComponent, ActivityComponent, DefaultActivityScreenModule>(intent) {

    override fun createActivityComponent(parentComponent: AppComponent): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(parentComponent)
                .defaultActivityModule(DefaultActivityModule(persistentScope))
                .activityNavigationModule(ActivityNavigationModule())
                .build()
    }

    override fun getParentComponent(): AppComponent {
        val app = persistentScope.screenState.activity.application as App
        return app.appComponent
    }

    override fun getActivityScreenModule(): DefaultActivityScreenModule {
        return DefaultActivityScreenModule(persistentScope)
    }
}