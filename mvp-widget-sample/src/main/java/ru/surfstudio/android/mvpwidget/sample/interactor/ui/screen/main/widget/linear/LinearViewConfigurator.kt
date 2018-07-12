package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.linear

import dagger.Component
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.base.configurator.WidgetScreenConfigurator
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.base.dagger.widget.WidgetScreenModule

/**
 * Простой пример конфигуратора для виджета
 * */
class LinearViewConfigurator : WidgetScreenConfigurator() {

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       widgetScreenModule: WidgetScreenModule) =
            DaggerLinearViewConfigurator_LinearViewComponent.builder()
                    .activityComponent(parentComponent)
                    .widgetScreenModule(widgetScreenModule)
                    .build()


    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [WidgetScreenModule::class])
    interface LinearViewComponent : ScreenComponent<LinearWidgetView>
}