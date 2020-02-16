package ru.surfstudio.android.recycler.decorator.sample.easydecor

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.recycler.decorator.Builder
import ru.surfstudio.android.recycler.decorator.base.OffsetDecor
import ru.surfstudio.android.recycler.decorator.base.ViewHolderDecor

@Suppress("UNCHECKED_CAST")
class BaseItemControllerDecoration<I : BaseItem<out RecyclerView.ViewHolder>>(
        private val baseViewHolderDecor: BaseViewHolderDecor<I>
) : ViewHolderDecor {

    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {

        val itemPosition = recyclerView.getChildAdapterPosition(view)

        val adapter = recyclerView.adapter as EasyAdapter

        val baseItem = adapter.getItem(itemPosition) as I

        baseViewHolderDecor.draw(canvas, view, recyclerView, state, baseItem)
    }
}

@Suppress("UNCHECKED_CAST")
class BaseItemControllerOffset<I : BaseItem<out RecyclerView.ViewHolder>>(
        private val baseViewHolderOffset: BaseViewHolderOffset<I>
) : OffsetDecor {

    override fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
        val itemPosition = recyclerView.getChildAdapterPosition(view)

        val adapter = recyclerView.adapter as EasyAdapter

        val baseItem = adapter.getItem(itemPosition) as I

        baseViewHolderOffset.getItemOffsets(outRect, view, recyclerView, state, baseItem)
    }
}

interface BaseViewHolderDecor<I : BaseItem<out RecyclerView.ViewHolder>> {
    fun draw(canvas: Canvas,
             view: View,
             recyclerView: RecyclerView,
             state: RecyclerView.State,
             baseItem: I)
}

interface BaseViewHolderOffset<I : BaseItem<out RecyclerView.ViewHolder>> {
    fun getItemOffsets(outRect: Rect,
                       view: View,
                       recyclerView: RecyclerView,
                       state: RecyclerView.State,
                       baseItem: I)
}


fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.overlay(baseItemControllerDecoration: D): Builder {
    return this.overlay(BaseItemControllerDecoration(baseItemControllerDecoration))
}

fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.overlay(pair: Pair<Int, D>): Builder {
    return this.overlay(pair.first to BaseItemControllerDecoration(pair.second))
}

fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.underlay(baseItemControllerDecoration: D): Builder {
    return this.underlay(BaseItemControllerDecoration(baseItemControllerDecoration))
}

fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.underlay(pair: Pair<Int, D>): Builder {
    return this.underlay(pair.first to BaseItemControllerDecoration(pair.second))
}

fun <D : BaseViewHolderOffset<out BaseItem<out RecyclerView.ViewHolder>>> Builder.offset(baseItemControllerOffset: D): Builder {
    return this.offset(BaseItemControllerOffset(baseItemControllerOffset))
}

fun <D : BaseViewHolderOffset<out BaseItem<out RecyclerView.ViewHolder>>> Builder.offset(pair: Pair<Int, D>): Builder {
    return this.offset(pair.first to BaseItemControllerOffset(pair.second))
}