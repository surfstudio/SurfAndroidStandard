package ru.surfstudio.standard.ui.screen.webview

import android.app.Activity
import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule
import ru.surfstudio.android.core.ui.base.dagger.CustomScreenModule
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule

/**
 * Конфигуратор экрана с вебвью
 */
class WebViewScreenConfigurator(activity: Activity, intent: Intent) :
        ActivityScreenConfigurator(activity, intent) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       activityScreenModule: ActivityScreenModule?,
                                       coreActivityScreenModule: CoreActivityScreenModule?,
                                       intent: Intent): ScreenComponent<*> =
            DaggerWebViewScreenConfigurator_WebViewScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .activityScreenModule(activityScreenModule)
                    .webViewScreenModule(WebViewScreenModule(WebViewRoute(intent)))
                    .coreActivityScreenModule(coreActivityScreenModule)
                    .build()

    override fun getName() = "Web View Screen"

    @PerScreen
    @Component(dependencies = arrayOf(ActivityComponent::class),
            modules = arrayOf(ActivityScreenModule::class, WebViewScreenModule::class))
    interface WebViewScreenComponent : ScreenComponent<WebViewActivityView> {
    }

    @Module
    internal class WebViewScreenModule(route: WebViewRoute) :
            CustomScreenModule<WebViewRoute>(route)
}