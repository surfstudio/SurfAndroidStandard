package ru.surfstudio.android.recycler.extension.slide

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper used to setup [RecyclerView] to work with [BindableSlidingViewHolder]'s.
 *
 * **Note: you should call `bind()` in `onCreate()` and `unbind()` in `onDestroy()` methods
 * to avoid memory leaks!**
 * */
class SlidingHelper {

    private var itemAttachStateListener: SlidingItemAttachStateListener? = null
    private var itemHorizontalScrollListener: SlidingItemHorizontalScrollListener? = null
    private var itemVerticalScrollListener: SlidingItemVerticalScrollListener? = null
    private var itemClickListener: SlidingItemClickListener? = null
    private var internalRv: RecyclerView? = null

    /**
     * Setup given [RecyclerView] to work with [BindableSlidingViewHolder]'s.
     *
     * Adding listeners to [RecyclerView].
     *
     *  **Note: to avoid memory leaks - don't forget to call `unbind()` in `onDestroy()` method!**
     * */
    fun bind(rv: RecyclerView) {
        internalRv = rv.also {
            initItemAttachStateListener(it)
            initItemHorizontalScrollListener(it)
            initItemVerticalScrollListener(it)
            initItemClickListener(it)
        }
    }

    /** Remove listeners from [RecyclerView] and clear references. */
    fun unbind() {
        internalRv?.let {
            releaseItemAttachStateListener(it)
            releaseItemHorizontalScrollListener(it)
            releaseItemVerticalScrollListener(it)
            releaseItemClickListener(it)
        }
        internalRv = null
    }

    private fun initItemAttachStateListener(rv: RecyclerView) {
        val watcher = SlidingItemAttachStateListener(rv).also { itemAttachStateListener = it }
        rv.addOnChildAttachStateChangeListener(watcher)
    }

    private fun initItemHorizontalScrollListener(rv: RecyclerView) {
        val listener = SlidingItemHorizontalScrollListener(rv).also { itemHorizontalScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun initItemVerticalScrollListener(rv: RecyclerView) {
        val listener = SlidingItemVerticalScrollListener(rv).also { itemVerticalScrollListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun initItemClickListener(rv: RecyclerView) {
        val listener = SlidingItemClickListener(rv).also { itemClickListener = it }
        rv.addOnItemTouchListener(listener)
    }

    private fun releaseItemAttachStateListener(rv: RecyclerView) {
        val listener = itemAttachStateListener ?: return
        rv.removeOnChildAttachStateChangeListener(listener)
        itemAttachStateListener = null
    }

    private fun releaseItemHorizontalScrollListener(rv: RecyclerView) {
        val listener = itemHorizontalScrollListener ?: return
        rv.removeOnItemTouchListener(listener)
        itemHorizontalScrollListener = null
    }

    private fun releaseItemVerticalScrollListener(rv: RecyclerView) {
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