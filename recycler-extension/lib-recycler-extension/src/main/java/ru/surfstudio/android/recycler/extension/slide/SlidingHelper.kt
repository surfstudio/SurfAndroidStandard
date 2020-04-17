package ru.surfstudio.android.recycler.extension.slide

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper used to properly setup [RecyclerView] and [BindableSlidingViewHolder].
 *
 * **Note**: you should call `bind()` on creation and `unbind()` on destroy.
 * */
class SlidingHelper {

    private var itemAttachStateListener: SlidingItemAttachStateListener? = null
    private var itemHorizontalScrollListener: SlidingItemHorizontalScrollListener? = null
    private var itemVerticalScrollListener: SlidingItemVerticalScrollListener? = null
    private var itemClickListener: SlidingItemClickListener? = null
    private var internalRv: RecyclerView? = null

    fun bind(rv: RecyclerView) {
        internalRv = rv.also {
            initItemAttachListener(it)
            initItemTouchListener(it)
            initItemTouchScrollListener(it)
            initItemClickListener(it)
        }
    }

    fun unbind() {
        internalRv?.let {
            releaseItemAttachListener(it)
            releaseItemTouchListener(it)
            releaseItemTouchScrollListener(it)
            releaseItemClickListener(it)
        }
        internalRv = null
    }

    private fun initItemAttachListener(rv: RecyclerView) {
        val watcher = SlidingItemAttachStateListener(rv).also { itemAttachStateListener = it }
        rv.addOnChildAttachStateChangeListener(watcher)
    }

    private fun initItemTouchListener(rv: RecyclerView) {
        val listener = SlidingItemHorizontalScrollListener(rv).also { itemHorizontalScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun initItemTouchScrollListener(rv: RecyclerView) {
        val listener = SlidingItemVerticalScrollListener(rv).also { itemVerticalScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun initItemClickListener(rv: RecyclerView) {
        val listener = SlidingItemClickListener(rv).also { itemClickListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun releaseItemAttachListener(rv: RecyclerView) {
        val listener = itemAttachStateListener ?: return
        rv.removeOnChildAttachStateChangeListener(listener)
        itemAttachStateListener = null
    }

    private fun releaseItemTouchListener(rv: RecyclerView) {
        val listener = itemHorizontalScrollListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemHorizontalScrollListener = null
    }

    private fun releaseItemTouchScrollListener(rv: RecyclerView) {
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