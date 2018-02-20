package ru.surfstudio.standard.ui.screen.webview

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.dagger.CoreActivityScreenModule
import ru.surfstudio.android.core.mvp.dagger.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule

/**
 * Конфигуратор экрана с вебвью
 */
class WebViewScreenConfigurator(intent: Intent) :
        ActivityScreenConfigurator(intent) {

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       coreActivityScreenModule: CoreActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> =
            DaggerWebViewScreenConfigurator_WebViewScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .activityScreenModule(activityScreenModule)
                    .webViewScreenModule(WebViewScreenModule(WebViewRoute(intent)))
                    .coreActivityScreenModule(coreActivityScreenModule)
                    .build()

    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class),
            modules = arrayOf(ActivityScreenModule::class, WebViewScreenModule::class))
    interface WebViewScreenComponent : ScreenComponent<WebViewActivityView>

    @Module
    internal class WebViewScreenModule(route: WebViewRoute) :
            CustomScreenModule<WebViewRoute>(route)
}