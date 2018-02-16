package ru.surfstudio.standard.app.dagger

import dagger.Component
import ru.surfstudio.android.core.ui.base.dagger.BaseCoreActivityComponent
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityModule
import ru.surfstudio.android.core.util.StringsProvider
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.interactor.analytics.AnalyticsService

/**
 * Created by makstuev on 30.01.2018.
 */

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(CoreActivityModule::class)])
interface ActivityComponent : BaseCoreActivityComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun analyticsService(): AnalyticsService
    fun stringsProvider(): StringsProvider
}