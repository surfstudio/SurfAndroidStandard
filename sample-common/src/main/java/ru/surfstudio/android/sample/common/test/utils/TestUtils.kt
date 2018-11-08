package ru.surfstudio.android.sample.common.test.utils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

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
 * и проверяющая, что значение каждой вью после клика равно указанному тексту
 */
fun performClick(textForCheck: String, @IdRes vararg viewIdResList: Int) {
    viewIdResList.forEach {
        onView(withId(it))
                .perform(click())
                .check(matches(withText(textForCheck)))
    }
}

/**
 * Функция, проверяющая, что text или hint для вью равны значению строкового ресурса,
 * и выполняющая ввод нового значения для вью.
 *
 * @param viewResId id вью, значения которой будут проверено, а затем изменено
 * @param oldTextResId id строкового ресурса, значение которого должно быть равно hint или text для вью
 * @param isHint флаг, указывающий, какое свойство для вью требуется проверить - hint или text
 * @param newText новое значение текста для вью
 */
fun checkAndInputText(
        @IdRes viewResId: Int,
        @StringRes oldTextResId: Int,
        isHint: Boolean,
        newText: String
) {
    onView(withId(viewResId))
            .check(matches(
                    if (isHint)
                        withHint(oldTextResId)
                    else
                        withText(oldTextResId)))
            .perform(typeText(newText), closeSoftKeyboard())
}

/**
 * Функция, проверяющая, что на экране отображается Toast с заданным сообщением
 */
fun checkIfToastIsVisible(@StringRes toastResId: Int) {
    onView(withText(toastResId))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
}