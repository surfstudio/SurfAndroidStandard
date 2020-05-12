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

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Утилиты для работы с разрешениями для инструментальных тестов
 */
object PermissionUtils {

    fun grantPermissions(vararg permissionNameList: String) {
        if (SdkUtils.isAtLeastMarshmallow()) {
            val command = "pm grant ${getApplicationContext<Context>().packageName} ${permissionNameList.joinToString()}"
            getInstrumentation().uiAutomation.executeShellCommand(command)
        }
    }
}