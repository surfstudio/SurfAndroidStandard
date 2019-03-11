package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

/**
 * Конфигуратор экрана [ListActivityView]
 */
class ListScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ListScreenModule::class])
    interface ListScreenComponent : ScreenComponent<ListActivityView> {
        //do nothing
    }

    @Module
    class ListScreenModule(route: ListActivityRoute) : DefaultCustomScreenModule<ListActivityRoute>(route)

    override fun createScreenComponent(parentComponent: DefaultActivityComponent,
                                       activityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerListScreenConfigurator_ListScreenComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultActivityScreenModule(activityScreenModule)
                .listScreenModule(ListScreenModule(ListActivityRoute()))
                .build()
    }
}
