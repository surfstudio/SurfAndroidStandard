package ru.surfstudio.android.recycler.decorator.sample.controllers

import android.content.Context
import android.graphics.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_message_layout.view.*
import kotlinx.android.synthetic.main.item_controller_message_time.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.controllers.ChatMessageDirection.INCOME
import ru.surfstudio.android.recycler.decorator.sample.easydecor.BaseViewHolderDecor
import ru.surfstudio.android.recycler.decorator.sample.easydecor.BaseViewHolderOffset
import ru.surfstudio.android.recycler.decorator.sample.toPx

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

class ChatDecor(private val context: Context) : BaseViewHolderDecor<BindableItem<ChatObject, ChatMessageController.Holder>> {

    private val outcomeColor = ContextCompat.getColor(context, R.color.material_50)
    private val incomeColor = ContextCompat.getColor(context, R.color.material_501)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val outcomeBubbleCorners =
            floatArrayOf(
                    8.toPx.toFloat(), 8.toPx.toFloat(),     // Top left radius in px
                    0f, 0f,                                 // Top right radius in px
                    8.toPx.toFloat(), 8.toPx.toFloat(),     // Bottom right radius in px
                    0f, 0f                                  // Bottom left radius in px
            )

    private val incomeBubbleCorners =
            floatArrayOf(
                    0f, 0f,                                 // Top left radius in px
                    8.toPx.toFloat(), 8.toPx.toFloat(),     // Top right radius in px
                    0f, 0f,                                 // Bottom right radius in px
                    8.toPx.toFloat(), 8.toPx.toFloat()      // Bottom left radius in px
            )

    private val path = Path()

    override fun draw(canvas: Canvas,
                      view: View,
                      recyclerView: RecyclerView,
                      state: RecyclerView.State,
                      baseItem: BindableItem<ChatObject, ChatMessageController.Holder>?) {

        //take view of ViewHolder for draw message bubble
        val messageAreaView = view.message_area_layout
        val messageAreaRect = RectF(
                messageAreaView.left.toFloat(),
                view.top.toFloat(),
                messageAreaView.right.toFloat(),
                view.bottom.toFloat()
        )

        path.reset()

        val nextItem =
                baseItem?.nextItem as? BindableItem<ChatObject, ChatMessageController.Holder>

        val isLastInGroup = baseItem?.data?.messageDirection != nextItem?.data?.messageDirection

        if (baseItem?.data?.messageDirection == INCOME) {
            paint.color = incomeColor

            //draw bubble on message background
            path.addRoundRect(messageAreaRect, incomeBubbleCorners, Path.Direction.CW)

            //draw triangle for last one message in group
            if(isLastInGroup) {
                //right bottom corner of bubble
                val (x, y) = messageAreaRect.right to messageAreaRect.bottom
                path.moveTo(x, y)
                path.lineTo(x + 8.toPx, y)
                path.lineTo(x, y - 8.toPx)
                path.close()
            }

        } else {
            paint.color = outcomeColor
            //draw bubble on message background
            path.addRoundRect(messageAreaRect, outcomeBubbleCorners, Path.Direction.CW)

            //draw triangle for last one message in group
            if(isLastInGroup) {
                //left bottom corner of bubble
                val (x, y) = messageAreaRect.left to messageAreaRect.bottom
                path.moveTo(x, y)
                path.lineTo(x - 8.toPx, y)
                path.lineTo(x, y - 8.toPx)
                path.close()
            }
        }

        canvas.drawPath(path, paint)
    }
}

class ChatDecorOffset : BaseViewHolderOffset<BindableItem<ChatObject, ChatMessageController.Holder>> {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                recyclerView: RecyclerView,
                                state: RecyclerView.State,
                                baseItem: BindableItem<ChatObject, ChatMessageController.Holder>?) {

        val previousItem =
                baseItem?.previousItem as? BindableItem<ChatObject, ChatMessageController.Holder>
        val topOffset = if (previousItem != null && previousItem.data.messageDirection == baseItem.data?.messageDirection) {
            8.toPx
        } else {
            16.toPx
        }
        outRect.set(0, topOffset, 0, 0)
    }

}

class ChatObject(val id: Int, val messageDirection: ChatMessageDirection)

enum class ChatMessageDirection {
    INCOME,
    OUTCOME
}