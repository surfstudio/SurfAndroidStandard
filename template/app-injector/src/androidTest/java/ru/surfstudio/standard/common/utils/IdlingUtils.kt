package ru.surfstudio.standard.common.utils

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource

/**
 * Утилиты для работы с [IdlingResource]
 */
object IdlingUtils {

    fun registerIdlingResource(idlingResource: IdlingResource) {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    fun unregisterIdlingResource(idlingResource: IdlingResource) {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}