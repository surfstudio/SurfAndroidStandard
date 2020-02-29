package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.base.OffsetDecor
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.ChatMessageController
import ru.surfstudio.android.recycler.decorator.sample.toPx

class ChatDecorOffset : OffsetDecor {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                recyclerView: RecyclerView,
                                state: RecyclerView.State) {

        val vh = recyclerView.getChildViewHolder(view)
        if(vh !is ChatMessageController.Holder) {
            return
        }
        outRect.set(0, 8.toPx, 0, 0)
    }
}