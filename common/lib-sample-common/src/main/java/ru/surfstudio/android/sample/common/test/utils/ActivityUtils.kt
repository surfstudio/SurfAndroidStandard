package ru.surfstudio.android.sample.common.test.utils

import android.app.Activity
import android.view.ViewGroup
import androidx.core.view.children
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.AssertionFailedError

/**
 * Утилиты для тестирования Activity
 */
object ActivityUtils {

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
        var currentActivity: Activity? = null
        onView(ViewMatchers.isRoot()).check { view, _ ->
            if (view is ViewGroup) {
                currentActivity = view.children
                        .first { it.context is Activity }
                        .context as Activity
            }
        }
        if (currentActivity == null)
            throw AssertionFailedError("can't find activity ${activityClass.name}")

        if (currentActivity!!::class.java != activityClass) {
            throw AssertionFailedError("activity ${activityClass.name} is not visible (current activity: ${currentActivity!!::class.java.name})")
        }
    }
}