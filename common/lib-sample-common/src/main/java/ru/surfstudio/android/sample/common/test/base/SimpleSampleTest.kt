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
package ru.surfstudio.android.sample.common.test.base

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils

/**
 * Базовый класс для простейших инструментальных тестов примеров,
 * тестирование которых заключается в простом запуске приложения и проверке, что она прошла успешно
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
open class SimpleSampleTest<T : Activity>(private val mainActivityClass: Class<T>) {

    @Test
    fun testSample() {
        ActivityUtils.launchActivity(mainActivityClass)
    }
}