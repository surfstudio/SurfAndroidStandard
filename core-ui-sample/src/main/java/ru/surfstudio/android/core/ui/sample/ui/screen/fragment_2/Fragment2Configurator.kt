package ru.surfstudio.android.core.ui.sample.ui.screen.fragment_2

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.fragment.ChildFragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultFragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class Fragment2Configurator(bundle: Bundle): DefaultFragmentScreenConfigurator(bundle) {

    @PerScreen
    @Component(
            dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, FragmentModule::class]
    )
    internal interface FragmentComponent : ScreenComponent<Fragment2View>

    @Module
    internal class FragmentModule(route: Fragment2Route) : DefaultCustomScreenModule<Fragment2Route>(route) {

        @Provides
        fun provideChildFragmentNavigator(
                activityProvider: ActivityProvider,
                fragmentProvider: FragmentProvider,
                screenEventDelegateManager: ScreenEventDelegateManager
        ): ChildFragmentNavigator = ChildFragmentNavigator(activityProvider, fragmentProvider, screenEventDelegateManager)
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: DefaultActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerFragment2Configurator_FragmentComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .fragmentModule(FragmentModule(Fragment2Route()))
                .build()
    }
}