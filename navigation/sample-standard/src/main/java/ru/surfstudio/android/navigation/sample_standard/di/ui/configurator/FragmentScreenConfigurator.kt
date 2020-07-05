package ru.surfstudio.android.navigation.sample_standard.di.ui.configurator

import android.os.Bundle
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

/**
 * Базовый конфигуратор для экрана, основанного на фрагменте
 */
abstract class FragmentScreenConfigurator(args: Bundle?) : BaseFragmentViewConfigurator<ActivityComponent?, DefaultFragmentScreenModule?>(args) {
    override fun getFragmentScreenModule(): DefaultFragmentScreenModule {
        return DefaultFragmentScreenModule(persistentScope)
    }

    override fun getParentComponent(): ActivityComponent {
        return (persistentScope.screenState.fragment.activity as CoreActivityInterface)
                .persistentScope
                .configurator
                .activityComponent as ActivityComponent
    }
}