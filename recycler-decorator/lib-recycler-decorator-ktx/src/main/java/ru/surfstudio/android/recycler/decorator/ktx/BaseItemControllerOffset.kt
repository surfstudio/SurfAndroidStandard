package ru.surfstudio.android.recycler.decorator.ktx

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.easyadapter.item.NoDataItem
import ru.surfstudio.android.recycler.decorator.Decorator

/**
 * Wrapper for connect Decorator.OffsetDecor and EasyAdapter
 */
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