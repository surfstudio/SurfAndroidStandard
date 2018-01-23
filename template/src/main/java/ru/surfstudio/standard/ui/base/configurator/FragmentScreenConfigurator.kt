package ru.surfstudio.standard.ui.base.configurator

import android.app.Activity
import android.os.Bundle
import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentScreenConfigurator
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.FragmentScreenModule

abstract class FragmentScreenConfigurator(baseActivity: Activity, args: Bundle) : BaseFragmentScreenConfigurator<ActivityComponent, FragmentScreenModule>(args) {

    private val baseActivity: BaseActivityInterface

    init {
        this.baseActivity = baseActivity as BaseActivityInterface
    }

    override fun getParentComponent(): ActivityComponent {
        return (baseActivity.baseActivityDelegate.screenConfigurator as ActivityConfigurator).activityComponent
    }

    override fun getFragmentScreenModule(): FragmentScreenModule {
        return FragmentScreenModule()
    }

}
