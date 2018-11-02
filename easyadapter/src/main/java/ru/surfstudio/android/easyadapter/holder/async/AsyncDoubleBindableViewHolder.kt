package ru.surfstudio.android.easyadapter.holder.async

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder

/**
 * Имплементация DoubleBindableViewHolder для асинхроного инфлейта layoutId
 */
abstract class AsyncDoubleBindableViewHolder<T1, T2>(
        parent: ViewGroup,
        @LayoutRes private val layoutId: Int,
        name: String,
        @LayoutRes stubLayoutId: Int = R.layout.async_stub_layout
) : DoubleBindableViewHolder<T1, T2>(inflateStubView(parent, stubLayoutId, name)), AsyncViewHolder {
    final override var isItemViewInflated = false

    final override var fadeInDuration: Long = 500L
    final override var fadeInAction: () -> Unit = {}

    final override var resizeDuration: Long = 1500L
    final override var resizeAction: () -> Unit = {}

    private var firstData: T1? = null
    private var secondData: T2? = null

    init {
        inflateItemView(itemView, layoutId, name) {
            if (firstData != null && secondData != null) bind(firstData!!, secondData!!)
        }
    }

    final override fun bind(firstData: T1, secondData: T2) {
        this.firstData = firstData
        this.secondData = secondData
        if (isItemViewInflated) bindInternal(firstData, secondData)

    }

    abstract fun bindInternal(firstData: T1, secondData: T2)
}