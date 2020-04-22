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
package ru.surfstudio.android.recycler.extension.slide

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper used to setup [RecyclerView] to work with [BindableSlidingViewHolder]'s.
 *
 * **Note for Fragments: you should call `bind()` in `onViewCreated()`/`onActivityCreated()`
 * and `unbind()` in `onDestroyView()` methods to avoid memory leaks!**
 * */
class SlidingHelper {

    private var itemAttachStateListener: SlidingItemAttachStateListener? = null
    private var itemHorizontalScrollListener: SlidingItemHorizontalScrollListener? = null
    private var itemVerticalScrollListener: SlidingItemVerticalScrollListener? = null
    private var itemClickListener: SlidingItemClickListener? = null
    private var internalRv: RecyclerView? = null

    /**
     * Setup given [RecyclerView] to work with [BindableSlidingViewHolder]'s.
     *
     * Adding listeners to [RecyclerView].
     *
     *  **Note for Fragments: to avoid memory leaks - don't forget to call `unbind()` in `onDestroyView()` method!**
     * */
    fun bind(rv: RecyclerView) {
        internalRv = rv.also {
            initItemAttachStateListener(it)
            initItemHorizontalScrollListener(it)
            initItemVerticalScrollListener(it)
            initItemClickListener(it)
        }
    }

    /** Remove listeners from [RecyclerView] and clear references. */
    fun unbind() {
        internalRv?.let {
            releaseItemAttachStateListener(it)
            releaseItemHorizontalScrollListener(it)
            releaseItemVerticalScrollListener(it)
            releaseItemClickListener(it)
        }
        internalRv = null
    }

    private fun initItemAttachStateListener(rv: RecyclerView) {
        val watcher = SlidingItemAttachStateListener(rv).also { itemAttachStateListener = it }
        rv.addOnChildAttachStateChangeListener(watcher)
    }

    private fun initItemHorizontalScrollListener(rv: RecyclerView) {
        val listener = SlidingItemHorizontalScrollListener(rv).also { itemHorizontalScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun initItemVerticalScrollListener(rv: RecyclerView) {
        val listener = SlidingItemVerticalScrollListener(rv).also { itemVerticalScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun initItemClickListener(rv: RecyclerView) {
        val listener = SlidingItemClickListener(rv).also { itemClickListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun releaseItemAttachStateListener(rv: RecyclerView) {
        val listener = itemAttachStateListener ?: return
        rv.removeOnChildAttachStateChangeListener(listener)
        itemAttachStateListener = null
    }

    private fun releaseItemHorizontalScrollListener(rv: RecyclerView) {
        val listener = itemHorizontalScrollListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemHorizontalScrollListener = null
    }

    private fun releaseItemVerticalScrollListener(rv: RecyclerView) {
        val listener = itemVerticalScrollListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemHorizontalScrollListener = null
    }

    private fun releaseItemClickListener(rv: RecyclerView) {
        val listener = itemClickListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemClickListener = null
    }
}