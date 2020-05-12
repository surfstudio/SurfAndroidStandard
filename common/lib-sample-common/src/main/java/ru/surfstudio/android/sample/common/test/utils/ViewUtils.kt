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

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

/**
 * Общие утилиты для тестирования View
 */
object ViewUtils {

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
     * и вызывающая другую функцию после клика на каждую вью
     */
    fun performClick(action: () -> Unit, @IdRes vararg viewIdResList: Int) {
        viewIdResList.forEach {
            onView(withId(it)).perform(click())
            action()
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
}