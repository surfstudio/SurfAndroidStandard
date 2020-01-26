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
package ru.surfstudio.android.easyadapter.diff

import androidx.recyclerview.widget.DiffUtil.DiffResult
import ru.surfstudio.android.easyadapter.diff.base.BaseDiffer
import ru.surfstudio.android.easyadapter.diff.base.DiffCallbackCreator
import ru.surfstudio.android.easyadapter.diff.base.DiffResultApplier
import ru.surfstudio.android.easyadapter.diff.base.Differ
import ru.surfstudio.android.easyadapter.diff.base.data.DiffResultBundle

/**
 * Default [Differ] which calculates [DiffResult] in the main thread.
 */
internal class DefaultDiffer(
        diffResultApplier: DiffResultApplier,
        diffCallbackCreator: DiffCallbackCreator
) : BaseDiffer(diffResultApplier, diffCallbackCreator) {

    override fun dispatchDiffResult(diffResultBundle: DiffResultBundle) {
        applyDiffResult(diffResultBundle)
    }
}