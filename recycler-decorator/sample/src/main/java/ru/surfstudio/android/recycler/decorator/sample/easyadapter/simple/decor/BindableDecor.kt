package ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.decor

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.controller.BindableController
import ru.surfstudio.android.recycler.decorator.sample.list.decor.Gap

class BindableDecor(val gap: Gap) : Decorator.ViewHolderDecor {

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val alpha = dividerPaint.alpha

    init {
        dividerPaint.color = gap.color
        dividerPaint.strokeWidth = gap.height.toFloat()
    }


    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {

        val vh = recyclerView.getChildViewHolder(view)
        val adapter = recyclerView.adapter as EasyAdapter
        val baseItem = adapter.getItem(vh.adapterPosition) as BindableItem<Int, BindableController.Holder>

        val startX = recyclerView.paddingLeft + gap.paddingStart
        val startY = view.bottom + view.translationY
        val stopX = recyclerView.width - recyclerView.paddingRight - gap.paddingEnd
        val stopY = startY

        dividerPaint.alpha = (view.alpha * alpha).toInt()

        val nextBaseItem = baseItem?.nextItem as? BindableItem<Int, BindableController.Holder>

        if(baseItem.data != nextBaseItem?.data ?: -1) {
            canvas.drawLine(startX.toFloat(), startY, stopX.toFloat(), stopY, dividerPaint)
        }
    }
}