package ru.surfstudio.standard.ui.base.configurator

import android.app.Activity
import android.content.Intent

import ru.surfstudio.android.core.ui.base.screen.activity.BaseActivityInterface
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityScreenConfigurator
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule

abstract class ActivityScreenConfigurator(baseActivity: Activity, intent: Intent) : BaseActivityScreenConfigurator<ActivityComponent, ActivityScreenModule>(intent) {

    private val baseActivity: BaseActivityInterface

    init {
        this.baseActivity = baseActivity as BaseActivityInterface
    }

    override fun getParentComponent(): ActivityComponent {
        return (baseActivity.baseActivityDelegate.screenConfigurator as ActivityConfigurator).activityComponent
    }

    override fun getActivityScreenModule(): ActivityScreenModule {
        return ActivityScreenModule()
    }
}
