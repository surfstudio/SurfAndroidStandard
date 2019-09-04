/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.easyadapter.holder.async

import android.view.View

/**
 * Interface that defines the required fields for ViewHolder asynchronous inflate.
 */
interface AsyncViewHolder {
    /**
     * Shows if the Main View successfully inflated.
     */
    var isItemViewInflated: Boolean

    /**
     * Defines the duration of the appear fade in animation.
     */
    var fadeInDuration: Long

    /**
     * Defines the function invoked after the Main View is inflated
     */
    fun onViewInflated(view: View) {}

    /**
     * Defines the function invoked after the Main View is completely appeared on the screen
     */
    fun onFadeInEnd() {}
}