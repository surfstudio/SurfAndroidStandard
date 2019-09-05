/*
  Copyright (c) 2018-present, SurfStudio LLC. Margarita Volodina, Oleg Zhilo.

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
package ru.surfstudio.android.security.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Handler

object AppDebuggableChecker {
    fun check(context: Context, checkDebuggable: Boolean) {
        if (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0 && checkDebuggable) {
            Handler().postDelayed({
                throw RuntimeException().apply {
                    stackTrace = arrayOfNulls(0)
                }
            }, 1000)
        }
    }
}