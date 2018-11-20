package ru.surfstudio.standard.app_injector.ui.screen.configurator.storage

import android.content.Intent
import android.os.Bundle
import ru.surfstudio.standard.app_injector.ui.configurator.*
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.MainScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.SplashScreenConfigurator
import ru.surfstudio.standard.f_main.MainActivityView
import ru.surfstudio.standard.f_splash.SplashActivityView
import kotlin.reflect.KClass

object ScreenConfiguratorStorage {

    val activityScreenConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityScreenConfigurator>()
            .apply {
                put(MainActivityView::class) { MainScreenConfigurator(it) }
                put(SplashActivityView::class) { SplashScreenConfigurator(it) }
            }

    val activityConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityConfigurator>()
            .apply {
            }

    val fragmentScreenConfiguratorMap = HashMap<KClass<*>, (args: Bundle) -> FragmentScreenConfigurator>()
            .apply {

            }

    val dialogScreenConfiguratorMap= HashMap<KClass<*>, (args: Bundle) -> DialogScreenConfigurator>()
            .apply {

            }

    val widgetScreenConfiguratorMap = HashMap<KClass<*>, () -> WidgetScreenConfigurator>()
            .apply {
            }
}