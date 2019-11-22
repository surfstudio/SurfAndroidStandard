/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Smirnov.

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
package ru.surfstudio.android.message

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Container for Snack parameters;
 * priority: resource id
 */
data class SnackParams(
        @StringRes
        val messageResId: Int = 0,
        val message: CharSequence = "",
        @ColorRes
        val backgroundColorResId: Int = 0,
        @ColorInt
        val backgroundColor: Int? = null,
        @StringRes
        val actionResId: Int = 0,
        val action: CharSequence = "",
        @ColorRes
        val actionColorResId: Int = 0,
        val actionColor: Int? = null,
        val duration: Int = Snackbar.LENGTH_SHORT
)