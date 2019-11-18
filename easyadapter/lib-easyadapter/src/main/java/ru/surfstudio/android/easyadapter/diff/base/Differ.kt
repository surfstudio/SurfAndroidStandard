package ru.surfstudio.android.easyadapter.diff.base

import androidx.recyclerview.widget.DiffUtil.DiffResult
import ru.surfstudio.android.easyadapter.diff.base.data.DiffCalculationBundle

/**
 * Interface of entity capable to calculate [DiffResult].
 */
internal interface Differ {

    /**
     * Initiate a diff calculation process.
     *
     * @param diffCalculationBundle bundle with data required for diff calculating.
     */
    fun calculateDiff(diffCalculationBundle: DiffCalculationBundle)
}