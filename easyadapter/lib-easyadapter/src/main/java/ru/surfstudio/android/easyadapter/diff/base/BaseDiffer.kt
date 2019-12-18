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