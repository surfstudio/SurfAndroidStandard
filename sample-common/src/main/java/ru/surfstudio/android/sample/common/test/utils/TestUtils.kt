package ru.surfstudio.android.sample.common.test.utils

import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import ru.surfstudio.android.sample.common.test.ToastMatcher

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