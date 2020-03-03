package ru.surfstudio.android.sample.common.test.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

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

    /**
     * Функция, выполняющая скролл RecyclerView до конца списка
     */
    fun scrollToBottom(@IdRes recyclerViewResId: Int) {
        onView(withId(recyclerViewResId))
                .perform(ScrollToBottomAction())
    }


    class ScrollToBottomAction : ViewAction {
        override fun getDescription(): String {
            return "scroll RecyclerView to bottom"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun perform(uiController: UiController?, view: View?) {
            val recyclerView = view as RecyclerView
            val itemCount = recyclerView.adapter?.itemCount
            val position = itemCount?.minus(1) ?: 0
            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()
        }
    }
}