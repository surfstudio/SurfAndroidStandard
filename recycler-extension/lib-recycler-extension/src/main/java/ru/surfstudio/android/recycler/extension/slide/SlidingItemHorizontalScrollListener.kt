/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
import android.view.VelocityTracker
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.extension.util.dpToPx
import kotlin.math.abs

/**
 * Listener used to intercept slide/swipe gesture on [BindableSlidingViewHolder].
 * */
class SlidingItemHorizontalScrollListener(private val rv: RecyclerView) :
        RecyclerView.OnItemTouchListener {

    private var viewHolder: BindableSlidingViewHolder<*>? = null
    private var velocityTracker: VelocityTracker? = null

    private var isInterceptionDisabled = false

    private var yDistanceToDisableInterceptionPx = rv.context.dpToPx(Y_DISTANCE_TO_DISABLE_INTERCEPTION_DP)
    private var startTouchX = 0f
    private var startTouchY = 0f

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        /* empty body */
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> onTouchInterceptionStarted(e)
            MotionEvent.ACTION_MOVE -> onTouchInterceptionMoved(e)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onTouchInterceptionEnded(e)
            else -> false
        }
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> onInterceptedTouchMoved(e)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onInterceptedTouchEnded(e)
        }
    }

    private fun onTouchInterceptionStarted(e: MotionEvent): Boolean {
        startTouchX = e.x
        startTouchY = e.y
        isInterceptionDisabled = false
        viewHolder = rv.findSlidingViewHolderUnder(startTouchX, startTouchY)
        return false
    }

    private fun onTouchInterceptionMoved(e: MotionEvent): Boolean {
        if (viewHolder == null || isInterceptionDisabled) return false

        val travelledDistanceY = abs(calculateDistanceBetween(e.y, startTouchY))
        if (travelledDistanceY >= yDistanceToDisableInterceptionPx) {
            isInterceptionDisabled = true
            return false
        }

        val travelledDistanceX = calculateDistanceBetween(e.x, startTouchX)
        val shouldIntercept = viewHolder?.onInterceptTouchEvent(travelledDistanceX) ?: false
        if (shouldIntercept) {
            startTouchX = e.x
            startTouchY = e.y

            velocityTracker?.clear()
            velocityTracker = velocityTracker ?: VelocityTracker.obtain()
            velocityTracker?.addMovement(e)

            viewHolder?.onSlidingStarted()
            rv.findVisibleSlidingViewHolders()
                    .filter { it != viewHolder }
                    .forEach { it.hideButtons(shouldAnimate = true) }

            return true
        }

        return false
    }

    private fun onTouchInterceptionEnded(e: MotionEvent): Boolean {
        velocityTracker?.recycle()
        velocityTracker = null
        viewHolder = null
        return false
    }

    private fun onInterceptedTouchMoved(e: MotionEvent) {
        velocityTracker?.addMovement(e)
        viewHolder?.onSliding(calculateDistanceBetween(e.x, startTouchX))
    }

    private fun onInterceptedTouchEnded(e: MotionEvent) {
        val velocityX = velocityTracker?.run {
            computeCurrentVelocity(1000)
            xVelocity
        } ?: 0f

        viewHolder?.onSlidingEnded(velocityX)
        velocityTracker?.recycle()
        velocityTracker = null
        viewHolder = null
    }

    private fun calculateDistanceBetween(target: Float, current: Float): Float {
        return target - current
    }

    private companion object {
        const val Y_DISTANCE_TO_DISABLE_INTERCEPTION_DP = 20
    }
}