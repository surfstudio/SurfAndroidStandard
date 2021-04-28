package ru.surfstudio.android.core.navigation.sample.ui.screen.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.customtabs.CustomTabsNavigator
import ru.surfstudio.android.core.ui.provider.Provider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор активити главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule {

        @Provides
        @PerScreen
        fun provideCustomTabsNavigator(activityProvider: Provider<AppCompatActivity>): CustomTabsNavigator =
                CustomTabsNavigator(activityProvider.get())
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule())
                .build()
    }
}
