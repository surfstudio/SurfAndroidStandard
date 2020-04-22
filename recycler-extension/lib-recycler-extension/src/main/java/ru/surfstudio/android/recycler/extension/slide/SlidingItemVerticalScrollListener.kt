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
import ru.surfstudio.android.recycler.extension.util.dpToPx
import kotlin.math.abs

/**
 * Listener used to hide visible buttons of [BindableSlidingViewHolder]'s on vertical scroll.
 * */
class SlidingItemVerticalScrollListener(private val rv: RecyclerView) :
        RecyclerView.OnItemTouchListener {

    private companion object {
        const val RESET_DISTANCE_DP = 40
    }

    private var distanceToResetPx = rv.context.dpToPx(RESET_DISTANCE_DP)
    private var isResetAllowed = false

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
            MotionEvent.ACTION_DOWN -> onInterceptionTouchStarted(e)
            MotionEvent.ACTION_MOVE -> onInterceptionTouchMoved(e)
            else -> false
        }
    }

    private fun onInterceptionTouchStarted(e: MotionEvent): Boolean {
        startTouchX = e.x
        startTouchY = e.y
        isResetAllowed = true
        return false
    }

    private fun onInterceptionTouchMoved(e: MotionEvent): Boolean {
        if (!isResetAllowed) return false
        val travelledDistanceY = abs(startTouchY - e.y)
        if (travelledDistanceY >= distanceToResetPx) {
            rv.findVisibleSlidingViewHolders().forEach { it.hideButtons(shouldAnimate = true) }
            isResetAllowed = false
        }
        return false
    }
}