package ru.surfstudio.android.security.sample.ui.screen.session

import android.content.Intent

import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.security.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.security.sample.ui.base.dagger.activity.CustomActivityComponent

/**
 * Конфигуратор экрана сессии
 */
class SessionScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, SessionScreenModule::class])
    internal interface SessionScreenComponent
        : ScreenComponent<SessionActivityView>

    @Module
    internal class SessionScreenModule(route: SessionActivityRoute)
        : DefaultCustomScreenModule<SessionActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       activityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerSessionScreenConfigurator_SessionScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(activityScreenModule)
                .sessionScreenModule(SessionScreenModule(SessionActivityRoute()))
                .build()
    }
}
