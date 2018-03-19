package ru.surfstudio.standard.ui.screen.widget.relative

import dagger.Component
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.WidgetScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.widget.WidgetScreenModule

/**
 * Простой пример конфигуратора для виджета
 * */
class RelativeViewConfigurator : WidgetScreenConfigurator() {

    override fun createScreenComponent(parentComponent: ActivityComponent, 
                                       widgetScreenModule: WidgetScreenModule) =
            DaggerRelativeViewConfigurator_RelativeViewComponent.builder()
                    .activityComponent(parentComponent)
                    .widgetScreenModule(widgetScreenModule)
                    .build()


    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [WidgetScreenModule::class])
    interface RelativeViewComponent : ScreenComponent<RelativeWidgetView>
}