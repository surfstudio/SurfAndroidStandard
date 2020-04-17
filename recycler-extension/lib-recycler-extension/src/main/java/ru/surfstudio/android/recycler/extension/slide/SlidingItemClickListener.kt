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

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        /** empty body. */
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        /** empty body. */
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> onInterceptTouchStart(e)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onInterceptTouchEnded(e)
            else -> false
        }
    }

    private fun onInterceptTouchStart(e: MotionEvent): Boolean {
        startTouchTime = System.currentTimeMillis()
        return false
    }

    private fun onInterceptTouchEnded(e: MotionEvent): Boolean {
        val endTouchTime = System.currentTimeMillis()
        val touchTime = endTouchTime - startTouchTime
        if (touchTime <= CLICK_MAX_TOUCH_TIME_MS) rv.slidingViewHoldersHideButtons()
        return false
    }
}