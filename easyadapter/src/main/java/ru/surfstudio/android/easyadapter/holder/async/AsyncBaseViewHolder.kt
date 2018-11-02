package ru.surfstudio.android.easyadapter.holder.async

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

/**
 * Имплементация BaseViewHolder для асинхроного инфлейта layoutId
 */
open class AsyncBaseViewHolder(
        parent: ViewGroup,
        @LayoutRes private val layoutId: Int,
        name: String,
        @LayoutRes stubLayoutId: Int = R.layout.async_stub_layout
) : BaseViewHolder(inflateStubView(parent, stubLayoutId, name)), AsyncViewHolder {
    final override var isItemViewInflated = false

    final override var fadeInDuration: Long = 500L
    final override var fadeInAction: () -> Unit = {}

    final override var resizeDuration: Long = 500L
    final override var resizeAction: () -> Unit = {}

    init {
        inflateItemView(itemView, layoutId, name)
    }
}