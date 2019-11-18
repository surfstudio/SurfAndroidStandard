/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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