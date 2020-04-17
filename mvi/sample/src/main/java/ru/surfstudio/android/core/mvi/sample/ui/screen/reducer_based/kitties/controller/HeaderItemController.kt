package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.controller

import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_kitties_header.view.*
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

internal class HeaderItemController(
        private val onBackClickedAction: () -> Unit
) : NoDataItemController<HeaderItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.list_item_kitties_header) {

        init {
            itemView.header_back_btn.setOnClickListener { onBackClickedAction() }
        }
    }
}