package ru.surfstudio.android.recycler.extension.sticky

import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.extension.sticky.controller.StickyBindableItemController
import ru.surfstudio.android.recycler.extension.sticky.item.StickyBindableItem

/**
 *  Расширения для работы sticky header с а [ItemList]
 */
fun <T> ItemList.addStickyHeader(data: T,
                                 itemController: StickyBindableItemController<T, out BindableViewHolder<T>>): ItemList {
    return addItem(StickyBindableItem(data, itemController))
}

fun <T> ItemList.addStickyHeaderIf(condition: Boolean,
                                   data: T,
                                   itemController: StickyBindableItemController<T, out BindableViewHolder<T>>): ItemList {
    return if (condition) addItem(StickyBindableItem(data, itemController)) else this
}

/**
 * Автоматизированное добавление Sticky Header в текущий лист с кастомизируемой политикой
 * добавления.
 *
 *
 * ВАЖНО: для корректного добавления Sticky Headers в лист требуется вызывать данный метод только
 * после того, как лист уже будет содержать в себе все необходимые данные для отображения.
 *
 *
 * Для работы со Sticky Headers требуется использовать [StickyEasyAdapter], который берёт
 * на себя всю ответственность по позиционированию заголовков.
 *
 * @param stickyCallback реализация текущей политики добавления Sticky Headers;
 * @param itemController sticky-контроллер;
 * @param <T>            тип значения, принимаемого sticky-контроллером
 * @return лист с добавленными в нужных местах Sticky Headers.
</T> */

fun <T> ItemList.addStickyHeaderIf(stickyCallback: (prev: Any?, next: Any) -> T?,
                                   itemController: StickyBindableItemController<T, out BindableViewHolder<T>>): ItemList {
    for (i in 0 until this.size) {
        var prevItem: BindableItem<*, *>? = null
        if (i > 0) {
            if (this[i - 1] !is BindableItem<*, *>) {
                continue
            }
            prevItem = this[i - 1] as BindableItem<*, *>
        }
        if (this[i] !is BindableItem<*, *>) {
            continue
        }
        val nextItem = this[i] as BindableItem<*, *>
        val stickyData = stickyCallback(if (prevItem != null) prevItem.getData() else null, nextItem.getData())
        if (stickyData != null) {
            insert(i, StickyBindableItem(stickyData, itemController))
        }
    }
    return this
}