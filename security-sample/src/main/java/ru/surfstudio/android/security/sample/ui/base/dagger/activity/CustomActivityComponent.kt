package ru.surfstudio.android.security.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule
import ru.surfstudio.android.security.sample.app.dagger.CustomAppComponent

/**
 * Компонент для @PerActivity скоупа
 */
@PerActivity
@Component(dependencies = [CustomAppComponent::class],
        modules = [DefaultActivityModule::class])
interface CustomActivityComponent : DefaultActivityComponent