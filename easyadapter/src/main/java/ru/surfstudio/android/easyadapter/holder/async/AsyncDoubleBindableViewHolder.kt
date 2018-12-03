package ru.surfstudio.android.easyadapter.holder.async

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.DoubleBindableViewHolder

/**
 * Имплементация DoubleBindableViewHolder для асинхроного инфлейта
 */
abstract class AsyncDoubleBindableViewHolder<T1, T2> private constructor(
        parent: ViewGroup,
        containerWidth: Int,
        containerHeight: Int
) : DoubleBindableViewHolder<T1, T2>(getContainer(parent, containerWidth, containerHeight)), AsyncViewHolder {

    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION

    private var firstData: T1? = null
    private var secondData: T2? = null

    private var isBindExecuted = false

    constructor(parent: ViewGroup,
                @LayoutRes layoutId: Int,
                @LayoutRes stubLayoutId: Int = R.layout.default_async_stub_layout,
                containerWidth: Int = DEFAULT_WIDTH,
                containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(itemView, layoutId) {
            if (isBindExecuted) bind(firstData, secondData)
        }
    }

    constructor(parent: ViewGroup,
                @LayoutRes layoutId: Int,
                stubView: View,
                containerWidth: Int = DEFAULT_WIDTH,
                containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        (itemView as ViewGroup).addView(stubView)
        inflateItemView(itemView, layoutId) {
            if (isBindExecuted) bind(firstData, secondData)
        }
    }

    final override fun bind(firstData: T1?, secondData: T2?) {
        isBindExecuted = true

        this.firstData = firstData
        this.secondData = secondData
        if (isItemViewInflated) bindInternal(firstData, secondData)
    }

    /**
     * Метод используется для биндинга данных вместо bind
     */
    abstract fun bindInternal(firstData: T1?, secondData: T2?)
}