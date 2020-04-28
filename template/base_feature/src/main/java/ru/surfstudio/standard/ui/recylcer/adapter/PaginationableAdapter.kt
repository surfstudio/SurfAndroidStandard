package ru.surfstudio.standard.ui.recylcer.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.template.base_feature.R

/**
 * Класс адаптера с поддержкой пагинации на основе EasyAdapter
 */
open class PaginationableAdapter(
        onShowMoreListener: () -> Unit
) : EasyPaginationAdapter(onShowMoreListener) {

    private var paginationFooterItemController: BasePaginationFooterController<*>? = null

    override fun getPaginationFooterController(): BasePaginationFooterController<*> {
        if (paginationFooterItemController == null)
            paginationFooterItemController = PaginationFooterItemController()
        return paginationFooterItemController!!
    }

    protected class PaginationFooterItemController : BasePaginationFooterController<PaginationFooterItemController.Holder>() {

        override fun createViewHolder(parent: ViewGroup, listener: OnShowMoreListener): Holder {
            return Holder(parent, listener)
        }

        inner class Holder(
                parent: ViewGroup,
                listener: OnShowMoreListener
        ) : BasePaginationFooterHolder(parent, R.layout.layout_pagination_footer) {

            val loadingIndicator: View = View(parent.context) //TODO loading
            val showMoreTv: TextView = itemView.findViewById(R.id.pagination_footer_text)

            init {
                showMoreTv.setOnClickListener { listener.onShowMore() }
                loadingIndicator.visibility = GONE
                showMoreTv.visibility = GONE
            }

            override fun bind(state: PaginationState) {

                //для пагинации на StaggeredGrid
                if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                    itemView.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> { isFullSpan = true }
                }

                when (state) {
                    PaginationState.READY -> {
                        loadingIndicator.visibility = VISIBLE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.COMPLETE -> {
                        loadingIndicator.visibility = GONE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.ERROR -> {
                        loadingIndicator.visibility = GONE
                        showMoreTv.visibility = VISIBLE
                    }
                    else -> throw IllegalArgumentException("unsupported state: $state")
                }
            }
        }
    }
}
