package ru.surfstudio.android.sample.common.test.utils

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent

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
        intended(hasComponent(activityClass.name))
    }
}