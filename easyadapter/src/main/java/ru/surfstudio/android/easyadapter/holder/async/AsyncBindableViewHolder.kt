package ru.surfstudio.android.easyadapter.holder.async

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

abstract class AsyncBindableViewHolder<T> private constructor(
        parent: ViewGroup
) : BindableViewHolder<T>(FrameLayout(parent.context)), AsyncViewHolder {
    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION
    final override var resizeDuration = DEFAULT_RESIZE_DURATION

    private var data: T? = null

    /**
     * Имплементация BindableViewHolder для асинхроного инфлейта layoutId
     *
     * @param layoutId айди ресурса для инфлейта основной вью
     * @param stubLayoutId айди ресурса для инфлейта вью, которая будет видна до появления основной
     */
    constructor(parent: ViewGroup,
                @LayoutRes layoutId: Int,
                @LayoutRes stubLayoutId: Int = R.layout.async_stub_layout
    ) : this(parent) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(parent, itemView, layoutId) {
            data?.let { bind(it) }
        }
    }

    final override fun bind(data: T) {
        this.data = data
        if (isItemViewInflated) bindInternal(data)
    }

    /**
     * Метод используется для биндинга данных вместо bind
     */
    abstract fun bindInternal(data: T)
}