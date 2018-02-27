package ru.surfstudio.android.recycler.extension.sticky.item


import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.recycler.extension.sticky.controller.StickyBindableItemController

class StickyBindableItem<T, H : BindableViewHolder<T>>(val data: T, itemController: StickyBindableItemController<T, H>)
    : BaseItem<H>(itemController), StickyHeader