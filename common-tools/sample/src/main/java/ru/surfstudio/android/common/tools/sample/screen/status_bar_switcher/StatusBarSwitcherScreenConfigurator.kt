package ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.common.tools.statusbarswitcher.StatusBarSwitcher
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.utilktx.ktx.ui.context.getDisplayMetrics

internal class StatusBarSwitcherScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, StatusBarSwitcherScreenModule::class]
    )
    internal interface StatusBarSwitcherScreenComponent : ScreenComponent<StatusBarSwitcherActivityView>

    @Module
    internal class StatusBarSwitcherScreenModule(
            route: StatusBarSwitcherActivityRoute
    ) : DefaultCustomScreenModule<StatusBarSwitcherActivityRoute>(route) {

        @Provides
        @PerScreen
        internal fun provideStatusBarSwitcher(context: Context): StatusBarSwitcher {
            return StatusBarSwitcher(context.getDisplayMetrics())
        }
    }

    override fun createScreenComponent(
            defaultActivityComponent: DefaultActivityComponent,
            defaultActivityScreenModule: DefaultActivityScreenModule,
            intent: Intent
    ): ScreenComponent<*> {
        return DaggerStatusBarSwitcherScreenConfigurator_StatusBarSwitcherScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .statusBarSwitcherScreenModule(StatusBarSwitcherScreenModule(StatusBarSwitcherActivityRoute()))
                .build()
    }
}
