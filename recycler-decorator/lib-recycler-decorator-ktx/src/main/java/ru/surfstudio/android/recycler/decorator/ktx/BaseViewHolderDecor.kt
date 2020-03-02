package ru.surfstudio.android.recycler.decorator.ktx

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.item.BaseItem

/**
 * Base ViewHolderDecor for EasyAdapter
 */
interface BaseViewHolderDecor<I: BaseItem<out RecyclerView.ViewHolder>> {

    fun draw(canvas: Canvas,
             view: View,
             recyclerView: RecyclerView,
             state: RecyclerView.State,
             baseItem: I?)
}