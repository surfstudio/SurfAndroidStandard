package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator

import android.content.Intent
import ru.surfstudio.standard.f_debug.common_controllers.CommonControllersDebugActivityView
import ru.surfstudio.standard.f_debug.debug.DebugActivityView
import ru.surfstudio.standard.f_debug.fcm.FcmDebugActivityView
import ru.surfstudio.standard.f_debug.injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.*
import ru.surfstudio.standard.f_debug.memory.MemoryDebugActivityView
import ru.surfstudio.standard.f_debug.server_settings.ServerSettingsDebugActivityView
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootActivityDebugView
import kotlin.reflect.KClass

object DebugScreenConfiguratorStorage {
    val activityScreenConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityScreenConfigurator>()
            .apply {
                put(DebugActivityView::class) { DebugScreenConfigurator(it) }
                put(FcmDebugActivityView::class) { FcmDebugScreenConfigurator(it) }
                put(CommonControllersDebugActivityView::class) { CommonControllersDebugScreenConfigurator(it) }
                put(ServerSettingsDebugActivityView::class) { ServerSettingsDebugScreenConfigurator(it) }
                put(RebootActivityDebugView::class) { RebootDebugScreenConfigurator(it) }
                put(MemoryDebugActivityView::class) { MemoryDebugScreenConfigurator(it) }
            }
}