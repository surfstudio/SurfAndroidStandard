package ru.surfstudio.standard.app_injector.ui.screen.configurator.storage

import android.content.Intent
import android.os.Bundle
import ru.surfstudio.standard.app_injector.ui.configurator.*
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug.FcmDebugScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug.DebugScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.MainScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.SplashScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug.CommonControllersDebugScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug.InfoDebugScreenConfigurator
import ru.surfstudio.standard.f_debug.debug.DebugActivityView
import ru.surfstudio.standard.f_debug.common_controllers.CommonControllersDebugActivityView
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityView
import ru.surfstudio.standard.f_debug.info.InfoDebugActivityView
import ru.surfstudio.standard.f_main.MainActivityView
import ru.surfstudio.standard.f_splash.SplashActivityView
import kotlin.reflect.KClass

object ScreenConfiguratorStorage {

    val activityScreenConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityScreenConfigurator>()
            .apply {
                put(MainActivityView::class) { MainScreenConfigurator(it) }
                put(SplashActivityView::class) { SplashScreenConfigurator(it) }
                put(DebugActivityView::class) { DebugScreenConfigurator(it) }
                put(FcmDebugActivityView::class) { FcmDebugScreenConfigurator(it) }
                put(CommonControllersDebugActivityView::class) { CommonControllersDebugScreenConfigurator(it) }
                put(InfoDebugActivityView::class) { InfoDebugScreenConfigurator(it) }
            }

    val activityConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityConfigurator>()
            .apply {
            }

    val fragmentScreenConfiguratorMap = HashMap<KClass<*>, (args: Bundle) -> FragmentScreenConfigurator>()
            .apply {

            }

    val dialogScreenConfiguratorMap = HashMap<KClass<*>, (args: Bundle) -> DialogScreenConfigurator>()
            .apply {

            }

    val widgetScreenConfiguratorMap = HashMap<KClass<*>, () -> WidgetScreenConfigurator>()
            .apply {
            }
}