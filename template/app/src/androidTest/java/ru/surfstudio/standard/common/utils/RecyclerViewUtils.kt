package ru.surfstudio.standard.common.utils

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*

/**
 * Утилиты для тестирования RecyclerView
 */
object RecyclerViewUtils {

    /**
     * Функция, выполняющая нажатие на элемент RecyclerView с заданной позицией
     */
    fun performItemClick(@IdRes recyclerViewResId: Int, position: Int) {
        onView(withId(recyclerViewResId))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    /**
     * Функция, выполняющая скролл RecyclerView на заданную позицию
     */
    fun scrollTo(@IdRes recyclerViewResId: Int, position: Int) {
        onView(withId(recyclerViewResId))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    /**
     * Функция, выполняющая скролл RecyclerView к элементу с заданным заголовком
     */
    fun scrollTo(@IdRes recyclerViewResId: Int, itemTitle: String) {
        onView(withId(recyclerViewResId))
                .perform(scrollTo<RecyclerView.ViewHolder>(
                        hasDescendant(withText(itemTitle)))
                )
    }
}