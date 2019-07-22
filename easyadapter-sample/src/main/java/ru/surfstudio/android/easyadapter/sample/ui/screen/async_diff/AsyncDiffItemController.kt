package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_async_diff.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R

class AsyncDiffItemController(
        private val onClickAction: () -> Unit
) : BindableItemController<Int, AsyncDiffItemController.Holder>() {

    override fun getItemId(data: Int): String = data.hashCode().toString()

    override fun createViewHolder(parentView: ViewGroup): Holder = Holder(parentView)

    inner class Holder(parentView: ViewGroup) : BindableViewHolder<Int>(
            parentView,
            R.layout.list_item_async_diff
    ) {

        init {
            itemView.setOnClickListener { onClickAction() }
        }

        override fun bind(data: Int) {
            itemView.async_diff_title_tv.text = data.toString()
        }
    }
}