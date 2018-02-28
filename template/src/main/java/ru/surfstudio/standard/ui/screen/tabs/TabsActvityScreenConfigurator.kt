package ru.surfstudio.standard.ui.screen.tabs

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule

/**
 * Конфигуратор активити главного экрана
 */
internal class TabsActvityScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class], modules = [ActivityScreenModule::class, TabsScreenModule::class])
    internal interface TabsScreenComponent : ScreenComponent<TabsActivityView>

    @Module
    internal class TabsScreenModule(route: TabsActivityRoute) :
            CustomScreenModule<TabsActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerTabsActvityScreenConfigurator_TabsScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .build()
    }
}
