package ru.surfstudio.android.easyadapter.diff.async.base

import ru.surfstudio.android.easyadapter.diff.base.Differ
import androidx.recyclerview.widget.DiffUtil.DiffResult

/**
 * [Differ] which capable to calculate [DiffResult] in a worker thread.
 */
internal interface AsyncDiffer : Differ