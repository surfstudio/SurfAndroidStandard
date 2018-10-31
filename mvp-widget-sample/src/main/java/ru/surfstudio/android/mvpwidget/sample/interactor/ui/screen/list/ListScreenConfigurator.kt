package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list

import android.content.Intent
import dagger.Component
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор экрана todo
 */
class ListScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class])
    interface ListScreenComponent : ScreenComponent<ListActivityView> {
        //do nothing
    }

    override fun createScreenComponent(parentComponent: DefaultActivityComponent,
                                       activityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerListScreenConfigurator_ListScreenComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultActivityScreenModule(activityScreenModule)
                .build()
    }
}
