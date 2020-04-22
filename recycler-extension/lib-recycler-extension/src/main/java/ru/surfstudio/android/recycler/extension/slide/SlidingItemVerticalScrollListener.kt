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