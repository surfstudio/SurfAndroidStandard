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