package ru.surfstudio.android.easyadapter.diff

import ru.surfstudio.android.easyadapter.diff.base.Differ
import ru.surfstudio.android.easyadapter.diff.base.BaseDiffer
import ru.surfstudio.android.easyadapter.diff.base.DiffCallbackCreator
import ru.surfstudio.android.easyadapter.diff.base.DiffResultApplier
import androidx.recyclerview.widget.DiffUtil.DiffResult

/**
 * Default [Differ] which calculates [DiffResult] in the main thread.
 */
internal class DefaultDiffer(
        diffResultApplier: DiffResultApplier,
        diffCallbackCreator: DiffCallbackCreator
) : BaseDiffer(diffResultApplier, diffCallbackCreator)