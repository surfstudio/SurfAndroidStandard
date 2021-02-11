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
package ru.surfstudio.android.core.ui.provider.resource

import android.graphics.drawable.Drawable
import androidx.annotation.*

/**
 * Abstract entity which can provide few types of Android resources.
 */
interface ResourceProvider {

    /**
     * Get a string by [id].
     *
     * @param id string resource id.
     * @param args arbitrary number of parameters for the obtaining string.
     */
    fun getString(@StringRes id: Int, vararg args: Any): String

    /**
     * Get a quantity-string (for given [quantity]) by [id].
     *
     * @param quantity quantity of items for the obtaining string.
     * @param args arbitrary number of parameters for the obtaining string.
     */
    fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg args: Any): String

    /**
     * Get a string list by [id].
     */
    fun getStringList(@ArrayRes id: Int): List<String>

    /**
     * Get a Drawable by [drawableRes].
     */
    fun getDrawable(@DrawableRes drawableRes: Int): Drawable?

    /**
     * Get a dimension value by [dimenRes].
     */
    fun getDimen(@DimenRes dimenRes: Int): Int
}
