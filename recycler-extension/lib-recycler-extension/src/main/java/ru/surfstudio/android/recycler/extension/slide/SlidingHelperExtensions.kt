package ru.surfstudio.android.recycler.extension.slide

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Extension-method used to find all visible [BindableSlidingViewHolder]'s.
 * */
internal fun RecyclerView.findVisibleSlidingViewHolders(): List<BindableSlidingViewHolder<*>> {
    val lm = layoutManager as? LinearLayoutManager ?: return emptyList()
    val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
    val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
    return mutableListOf<BindableSlidingViewHolder<*>>().apply {
        (firstVisibleItemPosition..lastVisibleItemPosition).forEach { position ->
            (findViewHolderForLayoutPosition(position) as? BindableSlidingViewHolder<*>)?.let(::add)
        }
    }
}

/** Extension-method used to find [BindableSlidingViewHolder] under specified position of screen. */
internal fun RecyclerView.findSlidingViewHolderUnder(
        x: Float,
        y: Float
): BindableSlidingViewHolder<*>? {
    val view = findChildViewUnder(x, y) ?: return null
    val viewHolder = findContainingViewHolder(view) ?: return null
    return viewHolder as? BindableSlidingViewHolder<*>
}