package ru.surfstudio.android.recycler.decorator.sample.easydecor

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.easyadapter.item.NoDataItem
import ru.surfstudio.android.recycler.decorator.Decorator

@Suppress("UNCHECKED_CAST")
class BaseItemControllerDecoration<I : BaseItem<out RecyclerView.ViewHolder>>(
        private val baseViewHolderDecor: BaseViewHolderDecor<I>
) : Decorator.ViewHolderDecor {

    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {

        val adapterPosition = recyclerView.getChildAdapterPosition(view)

        val adapter = recyclerView.adapter as EasyAdapter

        val baseItem = adapter.getItem(adapterPosition) as I

        if (adapter.isFirstInvisibleItemEnabled && baseItem is NoDataItem<*> && adapterPosition == 0) {
            return
        }

        baseViewHolderDecor.draw(canvas, view, recyclerView, state, baseItem as? I)
    }
}

class BaseItemControllerDecoration2<VH : BaseViewHolder>(
        private val baseViewHolderDecor: BaseViewHolderDecor2<VH>
) : Decorator.ViewHolderDecor {

    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {

        val adapterPosition = recyclerView.getChildAdapterPosition(view)

        val adapter = recyclerView.adapter as EasyAdapter

        val vh = recyclerView.getChildViewHolder(view)


        val baseItem = adapter.getItem(adapterPosition) as BaseItem<VH>

        if (adapter.isFirstInvisibleItemEnabled && baseItem is NoDataItem<*> && adapterPosition == 0) {
            return
        }

        baseViewHolderDecor.draw(canvas, view, recyclerView, state, baseItem)
    }
}

@Suppress("UNCHECKED_CAST")
class BaseItemControllerOffset<I : BaseItem<out RecyclerView.ViewHolder>>(
        private val baseViewHolderOffset: BaseViewHolderOffset<I>
) : Decorator.OffsetDecor {

    override fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
        val itemPosition = recyclerView.getChildAdapterPosition(view)

        val adapter = recyclerView.adapter as EasyAdapter

        val baseItem = adapter.getItem(itemPosition) as? I

        if (adapter.isFirstInvisibleItemEnabled && baseItem is NoDataItem<*> && itemPosition == 0) {
            return
        }

        baseItem?.let { item ->
            baseViewHolderOffset.getItemOffsets(outRect, view, recyclerView, state, item)
        }
    }
}

interface BaseViewHolderDecor<I: BaseItem<out RecyclerView.ViewHolder>> {

    fun draw(canvas: Canvas,
             view: View,
             recyclerView: RecyclerView,
             state: RecyclerView.State,
             baseItem: I?)
}

interface BaseViewHolderDecor2<VH: BaseViewHolder> {

    fun draw(canvas: Canvas,
             view: View,
             recyclerView: RecyclerView,
             state: RecyclerView.State,
             baseItem: BaseItem<VH>)
}

interface BaseViewHolderOffset<I : BaseItem<out RecyclerView.ViewHolder>> {
    fun getItemOffsets(outRect: Rect,
                       view: View,
                       recyclerView: RecyclerView,
                       state: RecyclerView.State,
                       baseItem: I?)
}

//fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.overlay(baseItemControllerDecoration: D): Builder {
//    return this.overlay(BaseItemControllerDecoration(baseItemControllerDecoration))
//}
//
//fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.overlay(pair: Pair<Int, D>): Builder {
//    return this.overlay(pair.first to BaseItemControllerDecoration(pair.second))
//}

//fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.underlay(baseItemControllerDecoration: D): Builder {
//    return this.underlay(BaseItemControllerDecoration(baseItemControllerDecoration))
//}

fun <I : BaseItem<out RecyclerView.ViewHolder>> Decorator.Builder.underlay(baseItemControllerDecoration: BaseViewHolderDecor<I>): Decorator.Builder {
    return this.underlay(BaseItemControllerDecoration(baseItemControllerDecoration))
}

//fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Builder.underlay(pair: Pair<Int, D>): Builder {
//    return this.underlay(pair.first to BaseItemControllerDecoration(pair.second))
//}

fun <D : BaseViewHolderOffset<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.offset(baseItemControllerOffset: D): Decorator.Builder {
    return this.offset(BaseItemControllerOffset(baseItemControllerOffset))
}

fun <D : BaseViewHolderOffset<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.offset(pair: Pair<Int, D>): Decorator.Builder {
    return this.offset(pair.first to BaseItemControllerOffset(pair.second))
}
