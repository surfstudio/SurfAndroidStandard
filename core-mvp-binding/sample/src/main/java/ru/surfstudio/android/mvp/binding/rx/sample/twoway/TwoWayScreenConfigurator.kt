package ru.surfstudio.android.mvp.binding.rx.sample.twoway

import android.content.Intent

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор [TwoWayActivityView].
 */
class TwoWayScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    override fun createScreenComponent(
            parentComponent: DefaultActivityComponent,
            activityScreenModule: DefaultActivityScreenModule,
            intent: Intent
    ): TwoWayScreenComponent = DaggerTwoWayScreenConfigurator_TwoWayScreenComponent
            .builder()
            .defaultActivityComponent(parentComponent)
            .defaultActivityScreenModule(activityScreenModule)
            .twoWayScreenModule(TwoWayScreenModule())
            .build()

    @PerScreen
    @Component(
            dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, TwoWayScreenModule::class]
    )
    interface TwoWayScreenComponent : BindableScreenComponent<TwoWayActivityView>

    @Module
    internal class TwoWayScreenModule {

        @Provides
        @PerScreen
        fun providePresenter(presenter: TwoWayActivityPresenter) = Any()
    }

}