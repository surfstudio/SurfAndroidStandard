package ru.surfstudio.android.navigation.sample_standard.screen.guide

import android.os.Bundle
import dagger.Component
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.configurator.FragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class GuideScreenConfigurator(args: Bundle?): FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class])
    internal interface GuideFragmentScreenComponent
        : ScreenComponent<GuideFragmentView>

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerGuideScreenConfigurator_GuideFragmentScreenComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .build()
    }
}