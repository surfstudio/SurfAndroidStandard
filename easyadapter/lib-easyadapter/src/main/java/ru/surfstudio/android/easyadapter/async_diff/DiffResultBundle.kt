package ru.surfstudio.android.easyadapter.async_diff

import androidx.recyclerview.widget.DiffUtil.DiffResult
import ru.surfstudio.android.easyadapter.ItemInfo
import ru.surfstudio.android.easyadapter.ItemList

/**
 * Bundle with calculated [DiffResult] and additional information.
 *
 * @property diffResult Calculated [DiffResult].
 * @property calculationBundle [DiffCalculationBundle] with information used for diff calculation.
 */
internal data class DiffResultBundle(
        val diffResult: DiffResult,
        val calculationBundle: DiffCalculationBundle
) {

    /**
     * @see DiffCalculationBundle.items
     */
    val items: ItemList
        get() = calculationBundle.items

    /**
     * @see DiffCalculationBundle.newItemInfo
     */
    val newItemInfo: List<ItemInfo>
        get() = calculationBundle.newItemInfo
}