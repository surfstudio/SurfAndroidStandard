package ru.surfstudio.standard.app_injector.ui.navigation

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import ru.surfstudio.standard.base_ui.navigation.MainActivityRoute
import ru.surfstudio.standard.f_main.MainActivityView
import kotlin.reflect.KClass

/**
 * Хранилище пограничных роутов в соответствии к классам.
 * Необходимо для навигации между модулями.
 *
 * Обращение происходит через {RouteClassProvider}
 */
object RouteClassStorage {

    val activityRouteMap = HashMap<KClass<*>, Class<out Activity>>()
            .apply {
                put(MainActivityRoute::class, MainActivityView::class.java)
            }

    val fragmentRouteMap = HashMap<KClass<*>, Class<out Fragment>>()
            .apply {
            }

    val dialogRouteMap = HashMap<KClass<*>, Class<out DialogFragment>>()
            .apply {
            }
}