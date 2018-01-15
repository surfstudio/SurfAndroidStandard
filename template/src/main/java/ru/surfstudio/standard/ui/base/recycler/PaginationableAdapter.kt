package ru.surfstudio.standard.ui.base.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.ui.base.recycler.BasePaginationableAdapter
import ru.surfstudio.android.core.ui.base.screen.model.state.PaginationState
import ru.surfstudio.standard.R

import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * адаптер для пагинируемых списков
 */
class PaginationableAdapter : BasePaginationableAdapter<RecyclerView.ViewHolder>() {

    private val paginationFooterItemController: PaginationFooterItemController by lazy {
        PaginationFooterItemController()
    }

    override fun getPaginationFooterController(): BasePaginationableAdapter.BasePaginationFooterController<*> {
        return paginationFooterItemController
    }

    protected class PaginationFooterItemController : BasePaginationableAdapter.BasePaginationFooterController<PaginationFooterItemController.Holder>() {

        override fun createViewHolder(parent: ViewGroup, listener: BasePaginationableAdapter.OnShowMoreListener): Holder {
            return Holder(parent, listener)
        }

        inner class Holder(parent: ViewGroup, listener: BasePaginationableAdapter.OnShowMoreListener) : BasePaginationableAdapter.BasePaginationFooterHolder(parent, R.layout.pagination_footer_layout) {

            private val progressBar: ProgressBar
            private val showMoreTv: TextView

            init {
                showMoreTv = itemView.findViewById(R.id.pagination_footer_text)
                progressBar = itemView.findViewById(R.id.pagination_footer_progress)
                showMoreTv.setOnClickListener { listener.onShowMore() }
                progressBar.visibility = GONE
                showMoreTv.visibility = GONE
            }

            override fun bind(state: PaginationState) {
                when (state) {
                    PaginationState.LOADING -> {
                        progressBar.visibility = VISIBLE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.COMPLETE, PaginationState.READY -> {
                        progressBar.visibility = GONE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.ERROR -> {
                        progressBar.visibility = GONE
                        showMoreTv.visibility = VISIBLE
                    }
                    else -> throw IllegalArgumentException("unsupported state: " + state)
                }
            }
        }
    }
}
