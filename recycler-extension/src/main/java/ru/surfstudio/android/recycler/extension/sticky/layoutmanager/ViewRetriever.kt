package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

internal interface ViewRetriever {

    fun getViewHolderForPosition(headerPositionToShow: Int): RecyclerView.ViewHolder?

    class RecyclerViewRetriever internal constructor(private val recyclerView: RecyclerView) : ViewRetriever {

        private var currentViewHolder: RecyclerView.ViewHolder? = null
        private var currentViewType: Int = 0

        init {
            this.currentViewType = -1
        }

        override fun getViewHolderForPosition(headerPositionToShow: Int): RecyclerView.ViewHolder? {
            if (currentViewType != recyclerView.adapter.getItemViewType(headerPositionToShow)) {
                currentViewType = recyclerView.adapter.getItemViewType(headerPositionToShow)
                currentViewHolder = recyclerView.adapter.createViewHolder(
                        recyclerView.parent as ViewGroup, currentViewType)
            }
            return currentViewHolder
        }
    }
}