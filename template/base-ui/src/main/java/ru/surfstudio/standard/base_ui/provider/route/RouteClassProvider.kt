package ru.surfstudio.standard.base_ui.provider.route

import android.app.Activity
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
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