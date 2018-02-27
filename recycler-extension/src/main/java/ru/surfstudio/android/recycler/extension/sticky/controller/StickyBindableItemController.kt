package ru.surfstudio.android.recycler.extension.sticky.controller


import ru.surfstudio.android.easyadapter.controller.BaseItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.sticky.item.StickyBindableItem

abstract class StickyBindableItemController<T, H : BindableViewHolder<T>> : BaseItemController<H, StickyBindableItem<T, H>>() {

    override fun bind(holder: H, item: StickyBindableItem<T, H>) {
        bind(holder, item.data)
    }

    fun bind(holder: H, data: T) {
        holder.bind(data)
    }

    override fun getItemId(item: StickyBindableItem<T, H>): Long {
        return getItemId(item.data)
    }

    override fun getItemHash(item: StickyBindableItem<T, H>): Long {
        return getItemHash(item.data)
    }

    fun getItemId(data: T): Long {
        return BaseItemController.NO_ID
    }

    fun getItemHash(data: T): Long {
        return data?.hashCode()?.toLong() ?: 0
    }
}