/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.utilktx.ktx.ui.view

import com.google.android.material.appbar.AppBarLayout
import ru.surfstudio.android.utilktx.util.SdkUtils


/**
 * Файл с extension-методами для работы с UI
 */

/**
 * Делает тень под тулбаром
 */
fun AppBarLayout.showElevation(isElevation: Boolean) {
    SdkUtils.runOnLollipop {
        elevation = if (isElevation) 8F else 0F
    }
}



