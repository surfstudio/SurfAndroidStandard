package ru.surfstudio.standard.app_injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.app_injector.ui.screen.CustomScreenModule
import ru.surfstudio.standard.base_ui.navigation.PushHandlerActivityRoute
import ru.surfstudio.standard.base_ui.notification.PushHandlerActivityView

class PushHandlerScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, PushHandlerScreenModule::class])
    interface PushHandlerScreenComponent
        : ScreenComponent<PushHandlerActivityView>

    @Module
    internal class PushHandlerScreenModule(route: PushHandlerActivityRoute)
        : CustomScreenModule<PushHandlerActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerPushHandlerScreenConfigurator_PushHandlerScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .pushHandlerScreenModule(PushHandlerScreenModule(PushHandlerActivityRoute(intent)))
                .build()
    }
}