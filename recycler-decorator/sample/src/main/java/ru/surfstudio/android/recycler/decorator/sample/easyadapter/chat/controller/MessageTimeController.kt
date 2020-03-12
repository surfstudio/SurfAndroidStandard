package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_controller_message_time.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.decorator.sample.R

class MessageTimeController : BindableItemController<String, MessageTimeController.Holder>() {

    override fun getItemId(data: String): String {
        return data
    }

    override fun createViewHolder(parent: ViewGroup): Holder {
        return Holder(parent)
    }

    class Holder(parent: ViewGroup) : BindableViewHolder<String>(parent, R.layout.item_controller_message_time) {
        override fun bind(data: String) {
            itemView.time_tv.text = data
        }
    }
}