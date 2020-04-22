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

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Extension-method used to find all visible [BindableSlidingViewHolder]'s.
 * */
internal fun RecyclerView.findVisibleSlidingViewHolders(): List<BindableSlidingViewHolder<*>> {
    val lm = layoutManager as? LinearLayoutManager ?: return emptyList()
    val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
    val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
    return mutableListOf<BindableSlidingViewHolder<*>>().apply {
        (firstVisibleItemPosition..lastVisibleItemPosition).forEach { position ->
            (findViewHolderForLayoutPosition(position) as? BindableSlidingViewHolder<*>)?.let(::add)
        }
    }
}

/** Extension-method used to find [BindableSlidingViewHolder] under specified position of screen. */
internal fun RecyclerView.findSlidingViewHolderUnder(
        x: Float,
        y: Float
): BindableSlidingViewHolder<*>? {
    val view = findChildViewUnder(x, y) ?: return null
    val viewHolder = findContainingViewHolder(view) ?: return null
    return viewHolder as? BindableSlidingViewHolder<*>
}