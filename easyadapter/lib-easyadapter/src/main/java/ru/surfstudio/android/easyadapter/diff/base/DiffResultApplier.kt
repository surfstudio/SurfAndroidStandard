package ru.surfstudio.android.easyadapter.diff.base

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.DiffUtil.DiffResult
import ru.surfstudio.android.easyadapter.diff.base.data.DiffResultBundle

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