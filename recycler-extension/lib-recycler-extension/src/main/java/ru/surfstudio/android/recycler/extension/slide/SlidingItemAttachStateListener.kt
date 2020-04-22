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

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Listener used to hide buttons of [BindableSlidingViewHolder] when it detached from window.
 * */
class SlidingItemAttachStateListener(private val rv: RecyclerView) :
        RecyclerView.OnChildAttachStateChangeListener {

    override fun onChildViewDetachedFromWindow(view: View) {
        val viewHolder = rv.findContainingViewHolder(view) as? BindableSlidingViewHolder<*>
        viewHolder?.hideButtons(shouldAnimate = false)
    }

    override fun onChildViewAttachedToWindow(view: View) {
        /* empty body. */
    }
}