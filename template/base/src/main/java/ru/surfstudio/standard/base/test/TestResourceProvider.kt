/*
  Copyright (c) 2018-present, SurfStudio LLC, Georgiy Kartashov.

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
package ru.surfstudio.standard.base.test

import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider

/**
 * Stub implementation of [ResourceProvider] for testing.
 */
class TestResourceProvider : ResourceProvider {

    override fun getString(id: Int, vararg args: Any) = "$id"

    override fun getQuantityString(id: Int, quantity: Int, vararg args: Any): String {
        return "$id|$quantity|${args.joinToString()}"
    }

    override fun getStringList(id: Int) = listOf("$id")

    override fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return null
    }

    override fun getDimen(@DimenRes dimenRes: Int): Int {
        return 0
    }

    override fun getInteger(@IntegerRes integerRes: Int): Int {
        return 0
    }
}
