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
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

/**
 * [BaseViewHolder] implementation for Asynchronous layout inflation
 */
open class AsyncBaseViewHolder private constructor(
    parent: ViewGroup,
    containerWidth: Int,
    containerHeight: Int
) : BaseViewHolder(getContainer(parent, containerWidth, containerHeight)), AsyncViewHolder {

    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION

    constructor(
            parent: ViewGroup,
            @LayoutRes layoutId: Int,
            @LayoutRes stubLayoutId: Int = R.layout.default_async_stub_layout,
            containerWidth: Int = DEFAULT_WIDTH,
            containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(itemView, layoutId)
    }

    constructor(
        parent: ViewGroup,
        @LayoutRes layoutId: Int,
        stubView: View,
        containerWidth: Int = DEFAULT_WIDTH,
        containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        (itemView as ViewGroup).addView(stubView)
        inflateItemView(itemView, layoutId)
    }
}