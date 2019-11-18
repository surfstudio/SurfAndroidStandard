package ru.surfstudio.android.easyadapter.diff.base

import android.os.Handler
import androidx.recyclerview.widget.DiffUtil
import ru.surfstudio.android.easyadapter.diff.base.data.DiffCalculationBundle
import ru.surfstudio.android.easyadapter.diff.base.data.DiffResultBundle

/**
 * Base [Differ].
 */
internal abstract class BaseDiffer(
        protected val diffResultApplier: DiffResultApplier,
        private val diffCallbackCreator: DiffCallbackCreator
) : Differ {

    private val handler = Handler()

    override fun calculateDiff(diffCalculationBundle: DiffCalculationBundle) {
        calculateDiffInternal(diffCalculationBundle)
    }

    protected open fun calculateDiffInternal(diffCalculationBundle: DiffCalculationBundle) {
        startDiffCalculation(diffCalculationBundle)
    }

    protected open fun applyDiffResult(diffResultBundle: DiffResultBundle) {
        diffResultApplier.apply(diffResultBundle)
    }

    protected open fun startDiffCalculation(diffCalculationBundle: DiffCalculationBundle) {
        computeDiff(diffCalculationBundle)
    }

    protected fun computeDiff(diffCalculationBundle: DiffCalculationBundle) {
        val diffResult = DiffUtil.calculateDiff(
                diffCallbackCreator.createDiffCallback(
                        diffCalculationBundle.oldItemInfo,
                        diffCalculationBundle.newItemInfo
                )
        )
        postDiffResult(DiffResultBundle(diffResult, diffCalculationBundle))
    }

    private fun postDiffResult(diffResultBundle: DiffResultBundle) {
        handler.post {
            applyDiffResult(diffResultBundle)
        }
    }
}