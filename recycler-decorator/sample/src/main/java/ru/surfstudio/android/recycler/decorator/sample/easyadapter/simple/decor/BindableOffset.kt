package ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.sample.easyadapter.simple.controller.BindableController
import ru.surfstudio.android.recycler.decorator.sample.toPx

class BindableOffset : Decorator.OffsetDecor {

    override fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
        val vh = recyclerView.getChildViewHolder(view)
        val adapter = recyclerView.adapter as EasyAdapter
        val baseItem = adapter.getItem(vh.adapterPosition) as BindableItem<Int, BindableController.Holder>

        if(baseItem?.data?.rem(2) == 0) {
            outRect.set(24.toPx, 0, 0, 0)
        }
        if(baseItem?.data?.rem(3) == 0) {
            outRect.set(48.toPx, 0, 0, 0)
        }
    }
}