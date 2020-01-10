package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.fragment

import android.os.Bundle
import dagger.Component
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultFragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

/**
 * Конфигуратор главного фрагмента
 */
class MainFragmentScreenConfigurator(bundle: Bundle): DefaultFragmentScreenConfigurator(bundle) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class])
    internal interface MainFragmentScreenComponent
        : ScreenComponent<MainFragmentView>

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: DefaultActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerMainFragmentScreenConfigurator_MainFragmentScreenComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .build()
    }
}