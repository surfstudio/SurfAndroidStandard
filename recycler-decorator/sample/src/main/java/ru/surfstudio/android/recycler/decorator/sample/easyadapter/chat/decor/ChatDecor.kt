package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_message_layout.view.*
import kotlinx.android.synthetic.main.item_controller_message_time.view.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.ChatMessageDirection
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.ChatObject
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.ChatMessageController
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.MessageTimeController
import ru.surfstudio.android.recycler.decorator.sample.toPx

class ChatDecor(private val context: Context) : Decorator.ViewHolderDecor {

    private val outcomeBubbleColor = ContextCompat.getColor(context, R.color.material_50)
    private val incomeBubbleColor = ContextCompat.getColor(context, R.color.material_501)
    private val timeBubbleColor = ContextCompat.getColor(context, R.color.gray_A150)

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
                      state: RecyclerView.State) {

        val vh = recyclerView.getChildViewHolder(view)
        val adapter = recyclerView.adapter as EasyAdapter
        val baseItem = adapter.getItem(vh.adapterPosition)

        when (vh) {
            is ChatMessageController.Holder -> decorMessageView(canvas, vh, recyclerView, view, baseItem as BindableItem<ChatObject, ChatMessageController.Holder>)
            is MessageTimeController.Holder -> decorTimeView(canvas, view)
        }
    }

    private fun decorTimeView(canvas: Canvas, view: View) {
        paint.color = timeBubbleColor

        val timeView = view.time_tv

        val textRectAreaOrigin = Rect()

        timeView.paint.getTextBounds(timeView.text.toString(), 0, timeView.text.length, textRectAreaOrigin)

        val textWith = textRectAreaOrigin.width()
        val textHeight = textRectAreaOrigin.height()

        val viewCy = (timeView.y + timeView.measuredHeight / 2).toInt()
        val viewCx = timeView.measuredWidth / 2

        //map text coordinates to canvas area
        val textRectAreaMapped = RectF(
                (viewCx - textWith / 2).toFloat(),
                (viewCy - textHeight / 2).toFloat(),
                (viewCx + textWith / 2).toFloat(),
                (viewCy + textHeight / 2).toFloat()
        )

        val scaleValue = textHeight * 0.45F

        textRectAreaMapped.inset(-scaleValue, -scaleValue)

        canvas.drawRoundRect(textRectAreaMapped, 10.toPx.toFloat(), 10.toPx.toFloat(), paint)
    }

    private fun decorMessageView(canvas: Canvas, vh: RecyclerView.ViewHolder, recyclerView: RecyclerView, view: View, baseItem: BindableItem<ChatObject, ChatMessageController.Holder>) {
        //take view of ViewHolder for draw message bubble
        val messageAreaView = view.message_area_layout
        val messageAreaRect = RectF(
                messageAreaView.left.toFloat(),
                view.top.toFloat(),
                messageAreaView.right.toFloat(),
                view.bottom.toFloat()
        )

        path.reset()
        val nextVh = recyclerView.findViewHolderForAdapterPosition(vh.adapterPosition + 1)


        val nextItem =
                baseItem.nextItem as? BindableItem<ChatObject, ChatMessageController.Holder>

        //(!) Frigile code, nextItem could not be not BindableItem<ChatObject, ChatMessageController.Holder>
        //You should check nextVh first of all
        val isLastInGroup = nextVh !is ChatMessageController.Holder ||
                baseItem.data?.messageDirection != nextItem?.data?.messageDirection

        if (baseItem.data?.messageDirection == ChatMessageDirection.INCOME) {
            paint.color = incomeBubbleColor

            //draw bubble on message background
            path.addRoundRect(messageAreaRect, incomeBubbleCorners, Path.Direction.CW)

            //draw triangle for last one message in group
            if (isLastInGroup) {
                //right bottom corner of bubble
                val (x, y) = messageAreaRect.right to messageAreaRect.bottom
                path.moveTo(x, y)
                path.lineTo(x + 8.toPx, y)
                path.lineTo(x, y - 8.toPx)
                path.close()
            }

        } else {
            paint.color = outcomeBubbleColor
            //draw bubble on message background
            path.addRoundRect(messageAreaRect, outcomeBubbleCorners, Path.Direction.CW)

            //draw triangle for last one message in group
            if (isLastInGroup) {
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