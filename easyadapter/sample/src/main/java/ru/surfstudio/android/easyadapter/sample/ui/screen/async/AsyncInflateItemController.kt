package ru.surfstudio.android.easyadapter.sample.ui.screen.async

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.async_inflate_list_item.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.async.AsyncBindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R

class AsyncInflateItemController(
        private val onClickAction: () -> Unit
) : BindableItemController<String, AsyncInflateItemController.Holder>() {

    override fun getItemId(data: String): String = data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(onClickAction, parent)

    class Holder(private val onClickAction: () -> Unit,
                 parent: ViewGroup
    ) : AsyncBindableViewHolder<String>(
            parent,
            R.layout.async_inflate_list_item,
            containerWidth = ViewGroup.LayoutParams.MATCH_PARENT
    ) {
        init {
            fadeInDuration = 500
        }

        override fun onViewInflated(view: View) {
            view.async_inflate_title_tv.setOnClickListener {
                onClickAction()
            }
        }

        override fun bindInternal(data: String?) {
            itemView.async_inflate_title_tv.text = data
        }
    }
}