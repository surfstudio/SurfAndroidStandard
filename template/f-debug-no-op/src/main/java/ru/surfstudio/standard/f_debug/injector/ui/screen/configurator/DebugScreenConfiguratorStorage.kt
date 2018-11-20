package ru.surfstudio.standard.f_debug.injector.ui.screen.configurator

import android.content.Intent
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import kotlin.reflect.KClass

object DebugScreenConfiguratorStorage {
    val activityScreenConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> BaseActivityViewConfigurator<*, *, *>>()
}