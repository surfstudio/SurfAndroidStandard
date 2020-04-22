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

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Listener used to hide buttons of [BindableSlidingViewHolder]
 * when user clicked on another item in list.
 * */
class SlidingItemClickListener(private val rv: RecyclerView) :
        RecyclerView.OnItemTouchListener {

    private companion object {
        const val CLICK_MAX_TOUCH_TIME_MS = 300L
    }

    private var startTouchTime = 0L
    private var startTouchX = 0f
    private var startTouchY = 0f

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        /** empty body. */
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        /** empty body. */
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> onInterceptTouchStarted(e)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onInterceptTouchEnded(e)
            else -> false
        }
    }

    private fun onInterceptTouchStarted(e: MotionEvent): Boolean {
        startTouchTime = System.currentTimeMillis()
        startTouchX = e.x
        startTouchY = e.y
        return false
    }

    private fun onInterceptTouchEnded(e: MotionEvent): Boolean {
        val endTouchTime = System.currentTimeMillis()
        val touchTime = endTouchTime - startTouchTime
        if (touchTime <= CLICK_MAX_TOUCH_TIME_MS) {
            val viewHolder = rv.findSlidingViewHolderUnder(startTouchX, startTouchY)
            rv.findVisibleSlidingViewHolders()
                    .filter { it != viewHolder }
                    .forEach { it.hideButtons(shouldAnimate = true) }
        }
        return false
    }
}