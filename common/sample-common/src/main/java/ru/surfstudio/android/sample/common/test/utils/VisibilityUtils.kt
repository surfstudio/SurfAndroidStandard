package ru.surfstudio.android.sample.common.test.utils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import ru.surfstudio.android.sample.common.test.ToastMatcher

private val SNACKBAR_ID = com.google.android.material.R.id.snackbar_text

/**
 * Утилиты для проверки видимости View
 */
object VisibilityUtils {

    /**
     * Функция, проверяющая, что на экране отображается Toast с заданным сообщением
     */
    fun checkIfToastIsVisible(@StringRes toastResId: Int) {
        onView(withText(toastResId))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что на экране отображается Toast с заданным сообщением
     */
    fun checkIfToastIsVisible(message: String) {
        onView(withText(message))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что на экране отображается Snackbar с заданным сообщением
     */
    fun checkIfSnackbarIsVisible(@StringRes messageResId: Int) {
        onView(withId(SNACKBAR_ID))
                .check(matches(withText(messageResId)))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая видимость вью, принадлежащей другой вью
     */
    fun checkIfViewIsVisible(@IdRes viewResId: Int, @IdRes parentViewResId: Int) {
        onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что на экране отображается вью с заданным id
     */
    fun checkIfViewIsVisible(@IdRes viewResId: Int) {
        onView(withId(viewResId))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что вью, принадлежащая другой вью, не видима
     */
    fun checkIfViewIsNotVisible(@IdRes viewResId: Int, @IdRes parentViewResId: Int) {
        onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
                .check(matches(not(isDisplayed())))
    }

    /**
     * Функция, проверяющая, что на экране не отображается вью с заданным id
     */
    fun checkIfViewIsNotVisible(@IdRes viewResId: Int) {
        onView(withId(viewResId))
                .check(matches(not(isDisplayed())))
    }
}