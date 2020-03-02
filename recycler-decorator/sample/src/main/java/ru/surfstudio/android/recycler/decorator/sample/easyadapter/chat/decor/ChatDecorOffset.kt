package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.ChatObject
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.ChatMessageController
import ru.surfstudio.android.recycler.decorator.sample.easydecor.BaseViewHolderOffset
import ru.surfstudio.android.recycler.decorator.sample.toPx

class ChatDecorOffset : BaseViewHolderOffset<BindableItem<ChatObject, ChatMessageController.Holder>> {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            recyclerView: RecyclerView,
            state: RecyclerView.State,
            baseItem: BindableItem<ChatObject, ChatMessageController.Holder>?
    ) {

        val vh = recyclerView.getChildViewHolder(view)
        if(vh !is ChatMessageController.Holder) {
            return
        }
        outRect.set(0, 8.toPx, 0, 0)
    }
}