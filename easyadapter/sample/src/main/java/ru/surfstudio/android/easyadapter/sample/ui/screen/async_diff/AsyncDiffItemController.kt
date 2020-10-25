package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_async_diff.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R

private const val ID_TAG = "AsyncDiffItemController"

class AsyncDiffItemController(
        private val onClickAction: () -> Unit
) : BindableItemController<Int, AsyncDiffItemController.Holder>() {

    override fun getItemId(data: Int): String = "$ID_TAG$data"

    override fun createViewHolder(parentView: ViewGroup): Holder = Holder(parentView)

    inner class Holder(parentView: ViewGroup) : BindableViewHolder<Int>(
            parentView,
            R.layout.list_item_async_diff
    ) {

        private val titleTv = itemView.async_diff_title_tv

        init {
            itemView.setOnClickListener { onClickAction() }
        }

        override fun bind(data: Int) {
            titleTv.text = data.toString()
        }
    }
}