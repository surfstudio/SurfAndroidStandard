package ru.surfstudio.android.easyadapter.async_diff

import android.os.Handler
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.Executors

/**
 * Base [AsyncDiffer].
 *
 */
internal abstract class BaseAsyncDiffer(
        protected val diffResultApplier: DiffResultApplier,
        private val diffCallbackCreator: DiffCallbackCreator
) : AsyncDiffer {

    private val handler = Handler()
    private val diffExecutor = Executors.newFixedThreadPool(DIFF_EXECUTOR_POOL_SIZE)

    override fun calculateDiff(diffCalculationBundle: DiffCalculationBundle) {
        calculateDiffInternal(diffCalculationBundle)
    }

    protected open fun calculateDiffInternal(diffCalculationBundle: DiffCalculationBundle) {
        startDiffCalculation(diffCalculationBundle)
    }

    protected open fun applyDiffResult(diffResultBundle: DiffResultBundle) {
        diffResultApplier.apply(diffResultBundle)
    }

    protected fun startDiffCalculation(diffCalculationBundle: DiffCalculationBundle) {
        val oldItemInfo = diffCalculationBundle.oldItemInfo
        val newItemInfo = diffCalculationBundle.newItemInfo

        diffExecutor.execute {
            val diffResult = DiffUtil.calculateDiff(
                    diffCallbackCreator.createDiffCallback(
                            oldItemInfo,
                            newItemInfo
                    )
            )
            postDiffResult(DiffResultBundle(diffResult, diffCalculationBundle))
        }
    }

    private fun postDiffResult(diffResultBundle: DiffResultBundle) {
        handler.post {
            applyDiffResult(diffResultBundle)
        }
    }

    private companion object {

        const val DIFF_EXECUTOR_POOL_SIZE = 2
    }
}