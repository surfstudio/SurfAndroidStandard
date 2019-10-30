package ru.surfstudio.android.easyadapter.async_diff

import androidx.recyclerview.widget.DiffUtil.DiffResult

/**
 * Interface of entity capable to calculate [DiffResult] in a worker thread.
 */
internal interface AsyncDiffer {

    /**
     * Initiate a diff calculation process.
     *
     * @param diffCalculationBundle bundle with data required for diff calculating.
     */
    fun calculateDiff(diffCalculationBundle: DiffCalculationBundle)
}