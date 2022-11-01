package ru.surfstudio.android.recycler.extension.sample.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.recycler.extension.sticky.layoutmanager.StickyHeaderHandler
import ru.surfstudio.android.recycler.extension.sticky.layoutmanager.StickyLayoutManager
import ru.surfstudio.android.sample.common.ui.base.easyadapter.PaginationFooterItemController

/**
 * Adapter с пагинацией и sticky header
 */
class PaginationStickyEasyAdapter(
        recyclerView: RecyclerView,
        onShowMoreListener: () -> Unit
) : EasyPaginationAdapter(PaginationFooterItemController(), onShowMoreListener) {

    private val stickyLayoutManager: StickyLayoutManager = StickyLayoutManager(
            recyclerView.context,
            object : StickyHeaderHandler {
                override fun getAdapterData(): List<*> {
                    return items
                }
            },
            false
    )

    init {
        recyclerView.layoutManager = stickyLayoutManager
        recyclerView.adapter = this
    }
}