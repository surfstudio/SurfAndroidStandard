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
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder

/**
 * [DoubleBindableViewHolder] implementation for Asynchronous layout inflation
 */
abstract class AsyncDoubleBindableViewHolder<T1, T2> private constructor(
    parent: ViewGroup,
    containerWidth: Int,
    containerHeight: Int
) : DoubleBindableViewHolder<T1, T2>(getContainer(parent, containerWidth, containerHeight)), AsyncViewHolder {

    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION

    private var firstData: T1? = null
    private var secondData: T2? = null

    private var isBindExecuted = false

    constructor(
            parent: ViewGroup,
            @LayoutRes layoutId: Int,
            @LayoutRes stubLayoutId: Int = R.layout.default_async_stub_layout,
            containerWidth: Int = DEFAULT_WIDTH,
            containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(itemView, layoutId) {
            if (isBindExecuted) bind(firstData, secondData)
        }
    }

    constructor(
        parent: ViewGroup,
        @LayoutRes layoutId: Int,
        stubView: View,
        containerWidth: Int = DEFAULT_WIDTH,
        containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        (itemView as ViewGroup).addView(stubView)
        inflateItemView(itemView, layoutId) {
            if (isBindExecuted) bind(firstData, secondData)
        }
    }

    /**
     * Bind the data only when the inflate is finished.
     *
     * Method is declared final to avoid [NullPointerException] in the inflation process
     *
     * @param firstData first data to be bound
     * @param secondData second data to be bound
     *
     * @see DoubleBindableViewHolder.bind
     */
    final override fun bind(firstData: T1?, secondData: T2?) {
        isBindExecuted = true

        this.firstData = firstData
        this.secondData = secondData
        if (isItemViewInflated) bindInternal(firstData, secondData)
    }

    /**
     * Safely bind the data to ViewHolder.
     *
     * @param firstData first data to be bound
     * @param secondData second data to be bound
     *
     * @see DoubleBindableViewHolder.bind
     */
    abstract fun bindInternal(firstData: T1?, secondData: T2?)
}