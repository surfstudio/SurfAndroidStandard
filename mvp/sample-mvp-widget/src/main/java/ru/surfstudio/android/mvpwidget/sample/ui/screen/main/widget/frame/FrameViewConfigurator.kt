package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.frame

import dagger.Component
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultWidgetScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.widget.DefaultWidgetScreenModule

/**
 * Простой пример конфигуратора для виджета
 */
class FrameViewConfigurator : DefaultWidgetScreenConfigurator() {

    override fun createScreenComponent(
            parentComponentDefault: DefaultActivityComponent,
            widgetScreenModule: DefaultWidgetScreenModule
    ): ScreenComponent<*> {
        return DaggerFrameViewConfigurator_FrameViewComponent.builder()
                .defaultActivityComponent(parentComponentDefault)
                .defaultWidgetScreenModule(widgetScreenModule)
                .build()
    }

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultWidgetScreenModule::class])
    interface FrameViewComponent : ScreenComponent<FrameWidgetView>
}