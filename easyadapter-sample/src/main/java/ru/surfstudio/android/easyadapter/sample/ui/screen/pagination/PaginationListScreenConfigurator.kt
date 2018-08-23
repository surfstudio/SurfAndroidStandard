package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class PaginationListScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, PaginationListScreenModule::class])
    internal interface PaginationListScreenComponent
        : ScreenComponent<PaginationListActivityView>

    @Module
    internal class PaginationListScreenModule(route: PaginationListActivityRoute)
        : DefaultCustomScreenModule<PaginationListActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerPaginationListScreenConfigurator_PaginationListScreenComponent.builder()
                .activityComponent(activityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .paginationListScreenModule(PaginationListScreenModule(PaginationListActivityRoute()))
                .build()
    }
}