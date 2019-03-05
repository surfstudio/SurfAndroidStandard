package ru.surfstudio.standard.ui.activity.di

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.standard.application.app.di.AppComponent
import ru.surfstudio.standard.application.app.di.AppProxyDependencies

/**
 * Компонент для @[PerActivity] скоупа
 */
@PerActivity
@Component(dependencies = [AppComponent::class],
        modules = [ActivityModule::class])
interface ActivityComponent : AppProxyDependencies, ActivityProxyDependencies