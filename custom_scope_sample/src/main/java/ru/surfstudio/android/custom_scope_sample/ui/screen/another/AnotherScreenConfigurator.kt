package ru.surfstudio.android.custom_scope_sample.ui.screen.another

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.configurator.LoginActivityScreenConfigurator
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.LoginActivityComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen

class AnotherScreenConfigurator(intent: Intent) : LoginActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [LoginActivityComponent::class],
            modules = [ActivityScreenModule::class, AnotherScreenModule::class])
    internal interface AnotherScreenComponent
        : ScreenComponent<AnotherActivityView>

    @Module
    internal class AnotherScreenModule(route: AnotherActivityRoute)
        : CustomScreenModule<AnotherActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(activityComponent: LoginActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerAnotherScreenConfigurator_AnotherScreenComponent.builder()
                .loginActivityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .anotherScreenModule(AnotherScreenModule(AnotherActivityRoute()))
                .build()
    }
}