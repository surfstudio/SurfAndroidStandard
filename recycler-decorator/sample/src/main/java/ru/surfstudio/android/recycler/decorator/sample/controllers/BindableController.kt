package ru.surfstudio.android.recycler.decorator.sample.controllers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_controller_short_card.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.sample.easydecor.BaseViewHolderDecor
import ru.surfstudio.android.recycler.decorator.sample.easydecor.BaseViewHolderOffset
import ru.surfstudio.android.recycler.decorator.sample.toPx
import ru.surfstudio.android.recycler.decorator.sample.sample.Gap

class BindableController(@LayoutRes val layoutRes: Int) : BindableItemController<Int, BindableController.Holder>() {

    override fun viewType(): Int {
        return layoutRes
    }

    override fun createViewHolder(parent: ViewGroup): Holder =
            Holder(parent, layoutRes)

    override fun getItemId(data: Int): String {
        return "$data"
    }

    class Holder(parent: ViewGroup, layoutRes: Int) : BindableViewHolder<Int>(parent, layoutRes) {
        override fun bind(number: Int) {
            itemView.number_tv.text = "$number"
        }
    }

    class BindableDecor(val gap: Gap) : BaseViewHolderDecor<BindableItem<Int, Holder>> {

        private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val alpha = dividerPaint.alpha

        init {
            dividerPaint.color = gap.color
            dividerPaint.strokeWidth = gap.height.toFloat()
        }


        override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State, baseItem: BindableItem<Int, Holder>) {

            val startX = recyclerView.paddingLeft + gap.paddingStart
            val startY = view.bottom + view.translationY
            val stopX = recyclerView.width - recyclerView.paddingRight - gap.paddingEnd
            val stopY = startY

            dividerPaint.alpha = (view.alpha * alpha).toInt()

            val nextBaseItem = baseItem.nextItem as? BindableItem<Int, Holder>

            if(baseItem.data != nextBaseItem?.data ?: -1) {
                canvas.drawLine(startX.toFloat(), startY, stopX.toFloat(), stopY, dividerPaint)
            }
        }
    }

    class BindableOffset : BaseViewHolderOffset<BindableItem<Int, Holder>> {
        override fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State, baseItem: BindableItem<Int, Holder>) {
            if(baseItem.data.rem(2) == 0) {
                outRect.set(24.toPx, 0, 0, 0)
            }
            if(baseItem.data.rem(3) == 0) {
                outRect.set(48.toPx, 0, 0, 0)
            }
        }
    }
}