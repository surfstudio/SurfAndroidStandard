package ru.surfstudio.android.sample.common.test.utils

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.allOf

/**
 * Утилиты для проверки отображения заданного текста на экране
 */
object TextUtils {

    /**
     * Функция, проверяющая, что text для вью, принадлежащей другой вью,
     * равен значению строкового ресурса
     */
    fun checkViewText(@IdRes viewResId: Int, @IdRes parentViewResId: Int, @StringRes textResId: Int) {
        onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
                .check(matches(withText(textResId)))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что text для вью, принадлежащей другой вью,
     * равен заданному значению
     */
    fun checkViewText(@IdRes viewResId: Int, @IdRes parentViewResId: Int, text: String) {
        onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
                .check(matches(withText(text)))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что text для вью равен значению строкового ресурса
     */
    fun checkViewText(@IdRes viewResId: Int, @StringRes textResId: Int) {
        onView(withId(viewResId))
                .check(matches(withText(textResId)))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что text для вью равен заданному значению
     */
    fun checkViewText(@IdRes viewResId: Int, text: String) {
        onView(withId(viewResId))
                .check(matches(withText(text)))
                .check(matches(isDisplayed()))
    }

    /**
     * Функция, проверяющая, что на экрана отображается вью с заданным текстом
     */
    fun checkText(@IdRes vararg textResIdList: Int) {
        textResIdList.forEach {
            onView(withText(it))
                    .check(matches(isDisplayed()))
        }
    }

    /**
     * Функция, проверяющая, что на экрана отображается вью с заданным текстом
     */
    fun checkText(vararg textList: String) {
        textList.forEach {
            onView(withText(it))
                    .check(matches(isDisplayed()))
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
     * Функция, возвращающая значение строкового ресурса
     */
    fun getString(@StringRes stringResId: Int): String {
        return InstrumentationRegistry.getInstrumentation().targetContext.getString(stringResId)
    }
}