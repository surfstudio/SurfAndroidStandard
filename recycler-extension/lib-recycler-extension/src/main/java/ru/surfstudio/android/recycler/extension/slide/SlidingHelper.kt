package ru.surfstudio.android.recycler.extension.slide

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper used to properly setup [RecyclerView] and [BindableSlidingViewHolder].
 *
 * **Note**: you should call `bind()` on creation and `unbind()` on destroy.
 * */
class SlidingHelper {

    private var itemAttachListener: SlidingItemAttachListener? = null
    private var itemTouchListener: SlidingItemTouchListener? = null
    private var itemTouchScrollListener: SlidingItemTouchScrollListener? = null
    private var internalRv: RecyclerView? = null

    fun bind(rv: RecyclerView) {
        internalRv = rv
        initItemAttachListener(rv)
        initItemTouchListener(rv)
        itemItemTouchScrollListener(rv)
    }

    fun unbind() {
        internalRv?.let {
            releaseItemAttachListener(it)
            releaseItemTouchListener(it)
            releaseItemTouchScrollListener(it)
        }
        internalRv = null
    }

    private fun initItemAttachListener(rv: RecyclerView) {
        val watcher = SlidingItemAttachListener(rv).also { itemAttachListener = it }
        rv.addOnChildAttachStateChangeListener(watcher)
    }

    private fun initItemTouchListener(rv: RecyclerView) {
        val listener = SlidingItemTouchListener(rv).also { itemTouchListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun itemItemTouchScrollListener(rv: RecyclerView) {
        val listener = SlidingItemTouchScrollListener(rv).also { itemTouchScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun releaseItemAttachListener(rv: RecyclerView) {
        val listener = itemAttachListener ?: return
        rv.removeOnChildAttachStateChangeListener(listener)
        itemAttachListener = null
    }

    private fun releaseItemTouchListener(rv: RecyclerView) {
        val listener = itemTouchListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemTouchListener = null
    }

    private fun releaseItemTouchScrollListener(rv: RecyclerView) {
        val listener = itemTouchScrollListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemTouchListener = null
    }
}