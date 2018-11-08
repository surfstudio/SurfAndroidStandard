package ru.surfstudio.android.sample.common.test.utils

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах
 */
fun performClick(@IdRes vararg viewIdResList: Int) {
    viewIdResList.forEach {
        Espresso.onView(ViewMatchers.withId(it)).perform(ViewActions.click())
    }
}