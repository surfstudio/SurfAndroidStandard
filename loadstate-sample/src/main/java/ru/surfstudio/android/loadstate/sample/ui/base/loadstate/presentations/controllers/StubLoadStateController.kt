package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers

import android.view.ViewGroup
import kotlinx.android.synthetic.main.stub_controller.view.*
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder
import ru.surfstudio.android.easyadapter.item.NoDataItem
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.logger.Logger


/**
 * Контроллер для пустых состний в списках
 * */

class StubLoadStateController() : NoDataItemController<StubLoadStateController.Holder>() {

    var loading: Boolean = false
        set(value) {
            field = value
            views.forEach { it.loading = value }
        }

    private val views = mutableSetOf<Holder>()

    override fun getItemId(item: NoDataItem<Holder>): String {
        Logger.v("ItemIds = $item")
        return item.toString()
    }

    override fun getItemHash(item: NoDataItem<Holder>?): String {
        return item.toString()
    }

    override fun createViewHolder(parent: ViewGroup) =
            Holder(parent).also {
                views.add(it)
            }

    inner class Holder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.stub_controller) {

        val shimmer = itemView.stub_controller_shimmer

        var loading: Boolean = false
            set(value) {
                field = value
                animateShimmer()
            }

        fun animateShimmer() {
            shimmer.post { shimmer.startShimmerAnimation() }
        }
    }
}