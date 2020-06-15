/*
  Copyright (c) 2020-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.sample.common.test.utils

import android.app.Activity
import android.view.ViewGroup
import androidx.core.view.children
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.AssertionFailedError

/**
 * Utils for [Activity] tests
 */
object ActivityUtils {

    /**
     * Function for launching a new screen
     */
    fun <T : Activity> launchActivity(activityClass: Class<T>) {
        ActivityScenario.launch(activityClass)
    }

    /**
     * Function which checks if the screen is visible
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