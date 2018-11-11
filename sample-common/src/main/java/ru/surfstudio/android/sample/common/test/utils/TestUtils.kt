package ru.surfstudio.android.sample.common.test.utils

import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

private val SNACKBAR_ID = com.google.android.material.R.id.snackbar_text

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
 * Функция, выполняющая нажатие на элемент RecyclerView с заданной позицией
 */
fun performClick(@IdRes recyclerViewResId: Int, position: Int) {
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
 * Функция, проверяющая, что text для вью, принадлежащей другой вью,
 * равен значению строкового ресурса
 */
fun checkViewText(@IdRes viewResId: Int, @IdRes parentViewResId: Int, @StringRes textResId: Int) {
    onView(allOf(withId(viewResId), isDescendantOfA(withId(parentViewResId))))
            .check(matches(withText(textResId)))
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
 * Функция, проверяющая, что на экрана отображается вью с заданным текстом
 */
fun checkText(@IdRes vararg textResIdList: Int) {
    textResIdList.forEach {
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

/**
 * Функция, открывающая новый экран
 */
fun <T : Activity> launchActivity(activityClass: Class<T>) {
    ActivityScenario.launch(activityClass)
}

/**
 * Функция, проверяющая, что экран является видимым
 */
fun <T> checkIfActivityIsVisible(activityClass: Class<T>) {
    Intents.intended(IntentMatchers.hasComponent(activityClass.name))
}

fun registerIdlingResource(idlingResource: IdlingResource) {
    IdlingRegistry.getInstance().register(idlingResource)
}

fun unregisterIdlingResource(idlingResource: IdlingResource) {
    IdlingRegistry.getInstance().unregister(idlingResource)
}