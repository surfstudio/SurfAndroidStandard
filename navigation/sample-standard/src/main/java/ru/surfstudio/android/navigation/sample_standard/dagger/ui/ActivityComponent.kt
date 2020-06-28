package ru.surfstudio.android.navigation.sample_standard.dagger.ui

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.navigation.sample_standard.dagger.AppComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

@PerActivity
@Component(
        dependencies = [AppComponent::class],
        modules = [DefaultActivityModule::class, ActivityNavigationModule::class]
)
interface ActivityComponent : DefaultActivityComponent, AppComponent