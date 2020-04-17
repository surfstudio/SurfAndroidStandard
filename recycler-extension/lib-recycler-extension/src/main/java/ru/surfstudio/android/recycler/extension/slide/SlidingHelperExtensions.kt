package ru.surfstudio.android.recycler.extension.slide

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** Extension-method used to find [BindableSlidingViewHolder] under specified position of screen. */
internal fun RecyclerView.findSlidingViewHolderUnder(
        x: Float,
        y: Float
): BindableSlidingViewHolder<*>? {
    val view = findChildViewUnder(x, y) ?: return null
    val viewHolder = findContainingViewHolder(view) ?: return null
    return viewHolder as? BindableSlidingViewHolder<*>
}

/**
 * Extension-method used to hide all of [BindableSlidingViewHolder] visible buttons in list.
 *
 * @param excludeList list of [BindableSlidingViewHolder],
 * that gonna be ignored and their state won't be changed.
 * */
internal fun RecyclerView.slidingViewHoldersHideButtons(
        excludeList: List<BindableSlidingViewHolder<*>?> = emptyList()
) {
    val lm = layoutManager as? LinearLayoutManager ?: return
    val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
    val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
    (firstVisibleItemPosition..lastVisibleItemPosition).forEach { position ->
        val slidingViewHolder = findViewHolderForLayoutPosition(position) as? BindableSlidingViewHolder<*>
        val isExcluded = excludeList.contains(slidingViewHolder)
        if (slidingViewHolder == null || isExcluded) return@forEach
        else slidingViewHolder.hideButtons()
    }
}