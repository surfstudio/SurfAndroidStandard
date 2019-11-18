package ru.surfstudio.android.easyadapter.diff.async

import ru.surfstudio.android.easyadapter.diff.async.base.AsyncDiffer
import ru.surfstudio.android.easyadapter.diff.async.base.BaseAsyncDiffer
import ru.surfstudio.android.easyadapter.diff.base.DiffCallbackCreator
import ru.surfstudio.android.easyadapter.diff.base.DiffResultApplier
import ru.surfstudio.android.easyadapter.diff.base.data.DiffCalculationBundle
import ru.surfstudio.android.easyadapter.diff.base.data.DiffResultBundle
import java.util.*

/**
 * [AsyncDiffer], which uses [AsyncDiffStrategy.APPLY_LATEST] strategy to handle
 * pending updates of a RecyclerView adapter backing item list.
 */
internal class ApplyLatestAsyncDiffer(
        diffResultApplier: DiffResultApplier,
        diffCallbackCreator: DiffCallbackCreator
) : BaseAsyncDiffer(diffResultApplier, diffCallbackCreator) {

    private val pendingUpdates: Deque<DiffCalculationBundle> = ArrayDeque()

    override fun calculateDiffInternal(diffCalculationBundle: DiffCalculationBundle) {
        pendingUpdates.push(diffCalculationBundle)
        if (pendingUpdates.size > 1) return

        startDiffCalculation(diffCalculationBundle)
    }

    override fun applyDiffResult(diffResultBundle: DiffResultBundle) {
        pendingUpdates.remove(diffResultBundle.calculationBundle)
        if (pendingUpdates.isNotEmpty()) {
            val latest = pendingUpdates.pop()
            pendingUpdates.clear()
            startDiffCalculation(latest)
        } else {
            diffResultApplier.apply(diffResultBundle)
        }
    }
}