package ru.surfstudio.android.navigation.sample_standard.screen.dialogs

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.navigation.sample_standard.di.ui.screen.ScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор активити главного экрана
 */
class DialogsScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, DialogsScreenModule::class])
    internal interface DialogsScreenComponent
        : BindableScreenComponent<DialogsActivityView>

    @Module
    internal class DialogsScreenModule : ScreenModule() {

        @Provides
        @PerScreen
        fun providePresenters(presenter: DialogsPresenter) = Any()
    }

    override fun createScreenComponent(defaultActivityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDialogsScreenConfigurator_DialogsScreenComponent.builder()
                .activityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .dialogsScreenModule(DialogsScreenModule())
                .build()
    }
}
