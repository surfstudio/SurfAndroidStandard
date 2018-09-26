package ru.surfstudio.android.custom_scope_sample.ui.screen.other

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.configurator.LoginActivityScreenConfigurator
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity.LoginActivityComponent
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen

class OtherScreenConfigurator(context: Context, intent: Intent) : LoginActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [LoginActivityComponent::class],
            modules = [ActivityScreenModule::class, OtherScreenModule::class])
    internal interface OtherScreenComponent
        : ScreenComponent<OtherActivityView>

    @Module
    internal class OtherScreenModule(route: OtherActivityRoute)
        : CustomScreenModule<OtherActivityRoute>(route)

    override fun createScreenComponent(
            parentActivityComponent: LoginActivityComponent?,
            activityScreenModule: ActivityScreenModule?,
            intent: Intent?
    ): ScreenComponent<*> {
        return DaggerOtherScreenConfigurator_OtherScreenComponent.builder()
                .activityScreenModule(activityScreenModule)
                .otherScreenModule(OtherScreenModule(OtherActivityRoute()))
                .build()
    }
}