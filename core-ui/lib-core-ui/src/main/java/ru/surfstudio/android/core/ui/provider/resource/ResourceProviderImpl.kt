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

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

/**
 * Default implementation of [ResourceProvider].
 */
class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getString(@StringRes id: Int, vararg args: Any): String {
        return if (args.isEmpty()) {
            context.resources.getString(id)
        } else {
            context.resources.getString(id, *args)
        }
    }

    override fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg args: Any): String {
        return if (args.isEmpty()) {
            context.resources.getQuantityString(id, quantity)
        } else {
            context.resources.getQuantityString(id, quantity, *args)
        }
    }

    override fun getStringList(@ArrayRes id: Int): List<String> {
        return context.resources.getStringArray(id).toList()
    }

    override fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableRes)
    }

    override fun getDimen(@DimenRes dimenRes: Int): Int {
        return context.resources.getDimensionPixelOffset(dimenRes)
    }

    override fun getInteger(@IntegerRes integerRes: Int): Int {
        return context.resources.getInteger(integerRes)
    }
}