package ru.surfstudio.android.easyadapter.async_diff

import androidx.recyclerview.widget.DiffUtil.Callback
import ru.surfstudio.android.easyadapter.ItemInfo

/**
 * Interface of [Callback] creator.
 */
@FunctionalInterface
internal interface DiffCallbackCreator {

    /**
     * Create [Callback].
     */
    fun createDiffCallback(
            oldItemInfo: List<ItemInfo>,
            newItemInfo: List<ItemInfo>
    ): Callback
}