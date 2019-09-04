package ru.surfstudio.android.security.sample.ui.screen.pin

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
 * Конфигуратор экрана ввода pin-кода
 */
class CreatePinScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, CreatePinScreenModule::class])
    internal interface CreatePinScreenComponent
        : ScreenComponent<CreatePinActivityView>

    @Module
    internal class CreatePinScreenModule(route: CreatePinActivityRoute)
        : DefaultCustomScreenModule<CreatePinActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       activityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerCreatePinScreenConfigurator_CreatePinScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(activityScreenModule)
                .createPinScreenModule(CreatePinScreenModule(CreatePinActivityRoute(intent)))
                .build()
    }
}
