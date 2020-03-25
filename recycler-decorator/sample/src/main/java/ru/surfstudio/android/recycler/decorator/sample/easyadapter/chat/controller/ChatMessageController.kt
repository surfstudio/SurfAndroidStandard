package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller

import android.view.Gravity
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chat_message_layout.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.ChatObject
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.ChatMessageDirection.INCOME

class ChatMessageController : BindableItemController<ChatObject, ChatMessageController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder {
        return Holder(parent)
    }

    override fun getItemId(data: ChatObject): String {
        return "${data.messageDirection.name} ${data.id}"
    }

    class Holder(parent: ViewGroup) : BindableViewHolder<ChatObject>(parent, R.layout.chat_message_layout) {
        override fun bind(data: ChatObject) {

            val gravity = if (data.messageDirection == INCOME) {
                Gravity.END
            } else {
                Gravity.START
            }

            itemView.chat_vh_root.gravity = gravity
            itemView.message_area_layout.gravity = gravity
        }
    }
}