package ru.surfstudio.android.sample.common.test.utils

import android.app.Activity
import android.view.ViewGroup
import androidx.core.view.children
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers

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

        if (currentActivity == null || currentActivity!!::class.java != activityClass) {
            throw RuntimeException("activity ${activityClass.name} is not visible")
        }
    }
}