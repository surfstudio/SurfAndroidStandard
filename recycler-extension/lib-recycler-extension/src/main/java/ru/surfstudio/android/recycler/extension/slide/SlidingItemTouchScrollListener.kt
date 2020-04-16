package ru.surfstudio.android.recycler.extension.slide

import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.extension.util.dpToPx
import kotlin.math.abs

/**
 * Listener used to hide visible buttons of [BindableSlidingViewHolder]'s on scroll.
 * */
class SlidingItemTouchScrollListener(rv: RecyclerView) : RecyclerView.OnItemTouchListener {

    private companion object {
        const val RESET_DISTANCE_DP = 40
    }

    private var distanceToResetPx = rv.context.dpToPx(RESET_DISTANCE_DP)
    private var isResetAllowed = false

    private var startTouchX = 0f
    private var startTouchY = 0f

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        /** empty body */
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        /** empty body */
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> onTouchStarted(rv, e.x, e.y)
            MotionEvent.ACTION_MOVE -> onTouchMoved(rv, e.x, e.y)
        }
        return false
    }

    private fun onTouchStarted(rv: RecyclerView, x: Float, y: Float) {
        startTouchX = x
        startTouchY = y
        isResetAllowed = true
    }

    private fun onTouchMoved(rv: RecyclerView, x: Float, y: Float) {
        if (!isResetAllowed) return
        val travelledDistanceY = abs(startTouchY - y)
        if (travelledDistanceY >= distanceToResetPx) {
            hideVisibleButtons(rv)
            isResetAllowed = false
        }
    }

    private fun hideVisibleButtons(rv: RecyclerView) {
        val layoutManager = rv.layoutManager as? LinearLayoutManager ?: return
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        (firstPosition..lastPosition).forEach { position ->
            val candidate = rv.findViewHolderForLayoutPosition(position) as? BindableSlidingViewHolder<*>
            candidate?.hideButtons()
        }
    }
}