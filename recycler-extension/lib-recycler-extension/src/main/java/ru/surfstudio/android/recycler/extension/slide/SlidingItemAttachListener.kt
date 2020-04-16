package ru.surfstudio.android.recycler.extension.slide

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Listener used to reset state of [BindableSlidingViewHolder]'s when they're detached from window.
 * */
class SlidingItemAttachListener(private val rv: RecyclerView) :
        RecyclerView.OnChildAttachStateChangeListener {

    override fun onChildViewDetachedFromWindow(view: View) {
        val viewHolder = rv.findContainingViewHolder(view) as? BindableSlidingViewHolder<*>
        viewHolder?.hideButtons(shouldAnimate = false)
    }

    override fun onChildViewAttachedToWindow(view: View) {
        /* empty body */
    }
}