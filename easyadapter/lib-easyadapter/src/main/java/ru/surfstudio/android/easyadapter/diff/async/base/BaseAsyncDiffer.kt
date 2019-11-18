package ru.surfstudio.android.easyadapter.diff.async.base

import ru.surfstudio.android.easyadapter.diff.base.BaseDiffer
import ru.surfstudio.android.easyadapter.diff.base.DiffCallbackCreator
import ru.surfstudio.android.easyadapter.diff.base.DiffResultApplier
import ru.surfstudio.android.easyadapter.diff.base.data.DiffCalculationBundle
import java.util.concurrent.Executors

/**
 * Base [AsyncDiffer].
 */
internal abstract class BaseAsyncDiffer(
        diffResultApplier: DiffResultApplier,
        diffCallbackCreator: DiffCallbackCreator
) : BaseDiffer(diffResultApplier, diffCallbackCreator), AsyncDiffer {

    private val diffExecutor = Executors.newFixedThreadPool(DIFF_EXECUTOR_POOL_SIZE)

    override fun startDiffCalculation(diffCalculationBundle: DiffCalculationBundle) {
        diffExecutor.execute {
            computeDiff(diffCalculationBundle)
        }
    }

    private companion object {

        const val DIFF_EXECUTOR_POOL_SIZE = 2
    }
}