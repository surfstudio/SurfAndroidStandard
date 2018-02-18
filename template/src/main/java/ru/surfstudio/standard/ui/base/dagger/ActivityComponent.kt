package ru.surfstudio.standard.ui.base.dagger

import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.dagger.BaseCoreActivityComponent
import ru.surfstudio.android.core.ui.dagger.CoreActivityModule
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.standard.app.dagger.AppComponent
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.interactor.analytics.AnalyticsService

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(CoreActivityModule::class)])
interface ActivityComponent : BaseCoreActivityComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun analyticsService(): AnalyticsService
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
}