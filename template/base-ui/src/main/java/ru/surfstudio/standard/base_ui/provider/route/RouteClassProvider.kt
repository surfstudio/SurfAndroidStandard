package ru.surfstudio.standard.base_ui.provider.route

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * Провайдер классов для роутов экранов
 */
object RouteClassProvider {

    lateinit var getFragmentClass:
            (kclass: KClass<*>) -> Class<out Fragment>

    lateinit var getDialogClass:
            (kclass: KClass<*>) -> Class<out DialogFragment>

    lateinit var getActivityClass: (kclass: KClass<*>) -> Class<out Activity>
}