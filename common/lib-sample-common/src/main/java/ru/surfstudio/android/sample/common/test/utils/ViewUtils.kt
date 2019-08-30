package ru.surfstudio.android.sample.common.test.utils

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

/**
 * Общие утилиты для тестирования View
 */
object ViewUtils {

    /**
     * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах
     */
    fun performClick(@IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
        }
    }

    /**
     * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах,
     * и вызывающая другую функцию после клика на каждую вью
     */
    fun performClick(action: () -> Unit, @IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
            action()
        }
    }

    /**
     * Функция, выполняющая нажатие на каждую вью, id которых переданы в параметрах,
     * и проверяющая, что значение каждой вью после клика равно указанному тексту
     */
    fun performClick(textForCheck: String, @IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it))
                    .perform(click())
                    .check(matches(withText(textForCheck)))
        }
    }
}