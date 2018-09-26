package ru.surfstudio.standard.app_injector.ui.screen.configurator.storage

import android.content.Intent
import android.os.Bundle
import ru.surfstudio.standard.app_injector.ui.configurator.*
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug.DebugScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.MainScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.configurator.activity.debug.DebugControllersScreenConfigurator
import ru.surfstudio.standard.f_debug.debug.DebugActivityView
import ru.surfstudio.standard.f_debug.debug_controllers.DebugControllersActivityView
import ru.surfstudio.standard.f_main.MainActivityView
import kotlin.reflect.KClass

object ScreenConfiguratorStorage {

    val activityScreenConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityScreenConfigurator>()
            .apply {
                put(MainActivityView::class) { MainScreenConfigurator(it) }
                put(DebugActivityView::class) { DebugScreenConfigurator(it) }
                put(DebugControllersActivityView::class) { DebugControllersScreenConfigurator(it) }
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