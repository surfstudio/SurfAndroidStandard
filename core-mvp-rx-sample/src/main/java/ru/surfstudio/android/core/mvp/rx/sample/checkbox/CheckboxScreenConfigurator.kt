package ru.surfstudio.android.core.mvp.rx.sample.checkbox

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
 * Конфигуратор активити главного экрана
 */
class CheckboxScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, CheckboxScreenModule::class])
    internal interface CheckboxScreenComponent
        : ScreenComponent<CheckboxActivityView>

    @Module
    internal class CheckboxScreenModule(route: CheckboxActivityRoute)
        : DefaultCustomScreenModule<CheckboxActivityRoute>(route)

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerCheckboxScreenConfigurator_CheckboxScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .checkboxScreenModule(CheckboxScreenModule(CheckboxActivityRoute()))
                .build()
    }
}
