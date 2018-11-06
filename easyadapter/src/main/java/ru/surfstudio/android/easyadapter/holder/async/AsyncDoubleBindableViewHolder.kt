package ru.surfstudio.android.easyadapter.holder.async

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.FrameLayout
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder

abstract class AsyncDoubleBindableViewHolder<T1, T2> private constructor(
        parent: ViewGroup
) : DoubleBindableViewHolder<T1, T2>(FrameLayout(parent.context)), AsyncViewHolder {
    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION
    final override var resizeDuration = DEFAULT_RESIZE_DURATION

    private var firstData: T1? = null
    private var secondData: T2? = null

    /**
     * Имплементация DoubleBindableViewHolder для асинхроного инфлейта layoutId
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
            if (firstData != null && secondData != null) bind(firstData!!, secondData!!)
        }
    }

    final override fun bind(firstData: T1, secondData: T2) {
        this.firstData = firstData
        this.secondData = secondData
        if (isItemViewInflated) bindInternal(firstData, secondData)

    }

    /**
     * Метод используется для биндинга данных вместо bind
     */
    abstract fun bindInternal(firstData: T1, secondData: T2)
}