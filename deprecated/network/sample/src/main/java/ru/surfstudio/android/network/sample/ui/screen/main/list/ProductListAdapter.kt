package ru.surfstudio.android.network.sample.ui.screen.main.list

import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.network.sample.R
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.easyadapter.pagination.PaginationState

class ProductListAdapter(
        onShowMoreListener: () -> Unit
) : EasyPaginationAdapter(onShowMoreListener) {

    private var paginationFooterItemController: BasePaginationFooterController<*>? = null

    override fun getPaginationFooterController(): BasePaginationFooterController<*> {
        if (paginationFooterItemController == null)
            paginationFooterItemController = PaginationFooterItemController()
        return paginationFooterItemController!!
    }

    private class PaginationFooterItemController
        : BasePaginationFooterController<PaginationFooterItemController.Holder>() {

        override fun createViewHolder(parent: ViewGroup, listener: OnShowMoreListener): Holder {
            return Holder(parent, listener)
        }

        inner class Holder(
                parent: ViewGroup,
                listener: OnShowMoreListener
        ) : BasePaginationFooterHolder(parent, R.layout.layout_pagination_footer) {

            private val loadingIndicator: MaterialProgressBar = itemView.findViewById(R.id.pagination_footer_progress_bar)
            private val showMoreTv: TextView = itemView.findViewById(R.id.pagination_footer_tv)

            init {
                showMoreTv.setOnClickListener { listener.onShowMore() }
                loadingIndicator.visibility = GONE
                showMoreTv.visibility = GONE
            }

            override fun bind(state: PaginationState) {
                loadingIndicator.isVisible = state == PaginationState.READY
                showMoreTv.isVisible = state == PaginationState.ERROR
            }
        }
    }
}