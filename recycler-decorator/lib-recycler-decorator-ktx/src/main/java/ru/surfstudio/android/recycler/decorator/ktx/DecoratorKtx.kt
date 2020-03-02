package ru.surfstudio.android.recycler.decorator.ktx

import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.recycler.decorator.Decorator

/**
 * Extension for [Decorator.Builder] for work with EasyAdapter
 */

fun <D : ru.surfstudio.android.recycler.decorator.ktx.BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.overlay(pair: Pair<Int, D>): Decorator.Builder {
    return this.overlay(pair.first to ru.surfstudio.android.recycler.decorator.ktx.BaseItemControllerDecoration(pair.second))
}

fun <D : ru.surfstudio.android.recycler.decorator.ktx.BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.underlay(pair: Pair<Int, D>): Decorator.Builder {
    return this.underlay(pair.first to ru.surfstudio.android.recycler.decorator.ktx.BaseItemControllerDecoration(pair.second))
}

fun <D : ru.surfstudio.android.recycler.decorator.ktx.BaseViewHolderOffset<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.offset(pair: Pair<Int, D>): Decorator.Builder {
    return this.offset(pair.first to ru.surfstudio.android.recycler.decorator.ktx.BaseItemControllerOffset(pair.second))
}