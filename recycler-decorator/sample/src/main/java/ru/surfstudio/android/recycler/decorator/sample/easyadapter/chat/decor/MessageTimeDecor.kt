package ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.decor

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_controller_message_time.view.*
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.sample.R
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.chat.controller.MessageTimeController
import ru.surfstudio.android.recycler.decorator.easyadapter.BaseViewHolderDecor
import ru.surfstudio.android.recycler.decorator.sample.toPx

class MessageTimeDecor(private val context: Context) : BaseViewHolderDecor<BindableItem<String, MessageTimeController.Holder>> {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            .apply {
                color = ContextCompat.getColor(context, R.color.gray_A150)
            }

    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State, baseItem: BindableItem<String, MessageTimeController.Holder>?) {

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
}