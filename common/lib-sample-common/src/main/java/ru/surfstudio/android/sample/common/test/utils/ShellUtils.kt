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

import androidx.test.platform.app.InstrumentationRegistry
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Utils for console commands launching on device for running tests
 */
object ShellUtils {

    fun executeCommands(vararg commandList: String) {
        if (SdkUtils.isAtLeastMarshmallow()) {
            commandList.forEach {
                InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(it)
            }
        }
    }
}