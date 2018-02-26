package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.support.v7.widget.RecyclerView

interface StickyHeaderHandler {

    /**
     * @return The dataset supplied to the [RecyclerView.Adapter]
     */
    fun getAdapterData(): List<*>
}