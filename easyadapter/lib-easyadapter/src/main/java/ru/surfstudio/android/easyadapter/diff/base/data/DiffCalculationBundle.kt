package ru.surfstudio.android.easyadapter.diff.base.data

import ru.surfstudio.android.easyadapter.ItemInfo
import ru.surfstudio.android.easyadapter.ItemList

/**
 * Bundle with all necessary data for diff calculating.
 *
 * @property items New [ItemList].
 * @property oldItemInfo List with previous RecyclerView adapter item list information.
 * @property oldItemInfo List with information about [items].
 */
internal data class DiffCalculationBundle(
        val items: ItemList,
        val oldItemInfo: List<ItemInfo>,
        val newItemInfo: List<ItemInfo>
)