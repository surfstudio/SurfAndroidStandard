package ru.surfstudio.easyadapter.sample.ui.common.recycler.pagination

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import ru.surfstudio.android.core.ui.base.recycler.pagination.BasePaginationableAdapter
import ru.surfstudio.android.core.ui.base.recycler.pagination.PaginationState
import ru.surfstudio.standard.R

class PaginationableAdapter<H : RecyclerView.ViewHolder> : BasePaginationableAdapter() {

    private var paginationFooterItemController: BasePaginationFooterController<*>? = null


    override fun getPaginationFooterController(): BasePaginationFooterController<*> {
        if (paginationFooterItemController == null) {
            paginationFooterItemController = PaginationFooterItemController()
        }
        return paginationFooterItemController!!
    }

    private class PaginationFooterItemController : BasePaginationFooterController<PaginationFooterItemController.Holder>() {

        override fun createViewHolder(parent: ViewGroup, listener: BasePaginationableAdapter.OnShowMoreListener): Holder {
            return Holder(parent, listener)
        }

        inner class Holder(
                parent: ViewGroup,
                listener: BasePaginationableAdapter.OnShowMoreListener
        ) : BasePaginationFooterHolder(parent, R.layout.pagination_footer_layout) {

            private val progressBar: ProgressBar
            private val showMoreTv: TextView

            init {
                showMoreTv = itemView.findViewById<TextView>(R.id.pagination_footer_text)
                progressBar = itemView.findViewById<ProgressBar>(R.id.pagination_footer_progress)
                showMoreTv.setOnClickListener { _ -> listener.onShowMore() }
                progressBar.visibility = GONE
                showMoreTv.visibility = GONE
            }

            override fun bind(state: PaginationState) {
                when (state) {
                    PaginationState.READY -> {
                        progressBar.visibility = VISIBLE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.COMPLETE -> {
                        progressBar.visibility = GONE
                        showMoreTv.visibility = GONE
                    }
                    PaginationState.ERROR -> {
                        progressBar.visibility = GONE
                        showMoreTv.visibility = VISIBLE
                    }
                    else -> throw IllegalArgumentException("Unsupported state: " + state)
                }
            }

            override fun animateRemove(): Boolean {
                itemView.animate()
                        .alpha(0f)
                        .setInterpolator(AccelerateInterpolator())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                itemView.alpha = 1f
                            }

                            override fun onAnimationCancel(animation: Animator?) {
                                itemView.alpha = 1f
                            }
                        })
                        .start()
                return false
            }


        }
    }
}