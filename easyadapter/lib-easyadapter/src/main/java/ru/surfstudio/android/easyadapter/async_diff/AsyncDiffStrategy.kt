package ru.surfstudio.android.easyadapter.async_diff

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.DiffUtil.DiffResult

/**
 * Enum with async diff calculation strategy types.
 *
 * A diff calculation strategy describes how to handle RecyclerView [Adapter] backing item list updates
 * during [DiffResult] async calculation.
 */
enum class AsyncDiffStrategy {

    /**
     * Skip all pending item list updates except latest.
     */
    APPLY_LATEST,

    /**
     * Add every item list updates in queue and then handle every item list update.
     */
    QUEUE_ALL
}