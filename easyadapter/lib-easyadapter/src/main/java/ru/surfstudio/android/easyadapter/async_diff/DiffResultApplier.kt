package ru.surfstudio.android.easyadapter.async_diff

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.DiffUtil.DiffResult

/**
 * Interface of entity capable to apply calculated [DiffResult] to [Adapter].
 */
@FunctionalInterface
internal interface DiffResultApplier {

    /**
     * Apply calculated [DiffResult].
     *
     * @param diffResultBundle bundle with calculated [DiffResult] and additional information.
     */
    fun apply(diffResultBundle: DiffResultBundle)
}