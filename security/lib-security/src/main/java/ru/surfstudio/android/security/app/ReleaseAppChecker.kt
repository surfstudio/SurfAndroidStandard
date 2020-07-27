/*
  Copyright (c) 2019-present, SurfStudio LLC. Margarita Volodina.

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

private const val CRASH_DELAY_MS = 1000L

/**
 * Object with different checks for release application.
 */
object ReleaseAppChecker {

    /**
     * Function for checking release application
     */
    fun checkReleaseApp(context: Context) {
        arrayOf(
                hasDebuggableFlags(context),
                !ReleaseSignatureChecker.isReleaseSignatureValid(context)
        ).forEach {
            // crash app for any failed check
            if (it) {
                crash()
            }
        }
    }

    private fun hasDebuggableFlags(context: Context): Boolean =
            context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private fun crash() {
        Handler().postDelayed({
            throw RuntimeException().apply {
                stackTrace = arrayOfNulls(0)
            }
        }, CRASH_DELAY_MS)
    }
}