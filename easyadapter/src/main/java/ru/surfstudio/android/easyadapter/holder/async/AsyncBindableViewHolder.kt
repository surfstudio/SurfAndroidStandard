package ru.surfstudio.android.easyadapter.holder.async

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

/**
 * Имплементация BindableViewHolder для асинхроного инфлейта layoutId
 */
abstract class AsyncBindableViewHolder<T>(
        parent: ViewGroup,
        @LayoutRes private val layoutId: Int,
        name: String,
        @LayoutRes stubLayoutId: Int = R.layout.async_stub_layout
) : BindableViewHolder<T>(inflateStubView(parent, stubLayoutId, name)), AsyncViewHolder {
    final override var isItemViewInflated = false

    final override var fadeInDuration: Long = 500L
    final override var fadeInAction: () -> Unit = {}

    final override var resizeDuration: Long = 500L
    final override var resizeAction: () -> Unit = {}

    private var data: T? = null

    init {
        inflateItemView(itemView, layoutId, name) {
            data?.let { bind(it) }
        }
    }

    final override fun bind(data: T) {
        this.data = data
        if (isItemViewInflated) bindInternal(data)
    }

    abstract fun bindInternal(data: T)
}