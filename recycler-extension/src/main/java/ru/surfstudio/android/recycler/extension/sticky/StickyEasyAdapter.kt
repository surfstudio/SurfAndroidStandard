package ru.surfstudio.android.recycler.extension.sticky

import android.support.v7.widget.RecyclerView

import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.recycler.extension.sticky.layoutmanager.StickyHeaderHandler
import ru.surfstudio.android.recycler.extension.sticky.layoutmanager.StickyLayoutManager

/**
 * EasyAdapter, упрощающий работу со Sticky Headers
 */
class StickyEasyAdapter(recyclerView: RecyclerView) : EasyAdapter() {

    init {
        val stickyLayoutManager = StickyLayoutManager(recyclerView.context,
                object : StickyHeaderHandler {
                    override fun getAdapterData(): List<*> {
                        return items
                    }
                }

        )
        recyclerView.layoutManager = stickyLayoutManager
        recyclerView.adapter = this
    }

}