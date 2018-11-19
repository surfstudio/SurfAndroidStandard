package ru.surfstudio.android.easyadapter.holder.async

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

/**
 * Имплементация BindableViewHolder для асинхроного инфлейта layoutId
 */
abstract class AsyncBindableViewHolder<T> private constructor(
        parent: ViewGroup,
        containerWidth: Int,
        containerHeight: Int
) : BindableViewHolder<T>(getContainer(parent, containerWidth, containerHeight)), AsyncViewHolder {
    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION

    private var data: T? = null

    private var isBindExecuted = false

    constructor(parent: ViewGroup,
                @LayoutRes layoutId: Int,
                @LayoutRes stubLayoutId: Int = R.layout.default_async_stub_layout,
                containerWidth: Int = DEFAULT_WIDTH,
                containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(itemView, layoutId) {
            if (isBindExecuted) bind(data)
        }
    }

    final override fun bind(data: T?) {
        isBindExecuted = true
        this.data = data
        if (isItemViewInflated) {
            bindInternal(data)
        }
    }

    /**
     * Метод используется для биндинга данных вместо bind
     */
    abstract fun bindInternal(data: T?)
}