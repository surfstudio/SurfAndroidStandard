package ru.surfstudio.standard.ui.widget.di

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator
import ru.surfstudio.android.mvp.widget.delegate.unwrapContext
import ru.surfstudio.standard.ui.activity.di.ActivityComponent

/**
 * Базовый конфигуратор для WidgetView
 */

abstract class WidgetScreenConfigurator : BaseWidgetViewConfigurator<ActivityComponent, WidgetScreenModule>() {

    override fun getWidgetScreenModule(): WidgetScreenModule {
        return WidgetScreenModule(persistentScope)
    }

    override fun getParentComponent(): ActivityComponent {
        return unwrapContext<CoreActivityInterface>(targetWidgetView.context)
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent() as ActivityComponent
    }
}