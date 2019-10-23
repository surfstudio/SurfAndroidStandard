package ru.surfstudio.android.easyadapter

import android.os.Handler
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.function.Consumer

internal class AsyncListDiffer {

    private val handler = Handler()
    private val executor = Executors.newSingleThreadExecutor()

    fun calculateDiff(
            diffCallback: DiffUtil.Callback,
            onDiffCalculatedAction: DiffUtilApplier
    ) {
        calculateDiffInternal(diffCallback, onDiffCalculatedAction)
    }

    private fun calculateDiffInternal(
            diffCallback: DiffUtil.Callback,
            onDiffCalculatedAction: DiffUtilApplier
    ) {
        executor.submit {
            val d = DiffUtil.calculateDiff(diffCallback)
            handler.post {
                onDiffCalculatedAction.apply(d)
            }
        }
    }
}

interface DiffUtilApplier {

    fun apply(diffResult: DiffUtil.DiffResult)
}