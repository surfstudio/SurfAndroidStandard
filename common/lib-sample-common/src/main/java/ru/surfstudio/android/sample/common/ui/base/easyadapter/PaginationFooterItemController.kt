package ru.surfstudio.android.sample.common.ui.base.easyadapter

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.sample.common.R

class PaginationFooterItemController
    : EasyPaginationAdapter.BasePaginationFooterController<PaginationFooterItemController.Holder>() {

    override fun createViewHolder(
            parent: ViewGroup,
            listener: EasyPaginationAdapter.OnShowMoreListener
    ): Holder {
        return Holder(parent, listener)
    }

    inner class Holder(
            parent: ViewGroup,
            listener: EasyPaginationAdapter.OnShowMoreListener
    ) : EasyPaginationAdapter.BasePaginationFooterHolder(parent, R.layout.layout_pagination_footer) {

        private val loadingIndicator: ProgressBar = itemView.findViewById(R.id.pagination_footer_progress_bar)
        private val showMoreTv: TextView = itemView.findViewById(R.id.pagination_footer_tv)

        init {
            showMoreTv.setOnClickListener { listener.onShowMore() }
            loadingIndicator.visibility = View.GONE
            showMoreTv.visibility = View.GONE
        }

        override fun bind(state: PaginationState) {
            // for StaggeredGrid pagination
            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                itemView.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> { isFullSpan = true }
            }

            when (state) {
                PaginationState.READY -> {
                    loadingIndicator.visibility = View.VISIBLE
                    showMoreTv.visibility = View.GONE
                }
                PaginationState.COMPLETE -> {
                    loadingIndicator.visibility = View.GONE
                    showMoreTv.visibility = View.GONE
                }
                PaginationState.ERROR -> {
                    loadingIndicator.visibility = View.GONE
                    showMoreTv.visibility = View.VISIBLE
                }
                else -> throw IllegalArgumentException("unsupported state: $state")
            }
        }
    }
}