package ru.surfstudio.android.recycler.decorator.easyadapter

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.easyadapter.item.NoDataItem
import ru.surfstudio.android.recycler.decorator.Decorator

/**
 * Wrapper for connect Decorator.ViewHolderDecor and EasyAdapter
 */
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