package ru.surfstudio.android.easyadapter.holder.async

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

open class AsyncBaseViewHolder private constructor(
        parent: ViewGroup,
        containerWidth: Int,
        containerHeight: Int
) : BaseViewHolder(getContainer(parent, containerWidth, containerHeight)), AsyncViewHolder {
    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION

    /**
     * Имплементация BaseViewHolder для асинхроного инфлейта layoutId
     *
     * @param layoutId айди ресурса для инфлейта основной вью
     * @param stubLayoutId айди ресурса для инфлейта вью, которая будет видна до появления основной
     */
    constructor(parent: ViewGroup,
                @LayoutRes layoutId: Int,
                @LayoutRes stubLayoutId: Int = R.layout.default_async_stub_layout,
                containerWidth: Int = DEFAULT_WIDTH,
                containerHeight: Int = DEFAULT_HEIGHT
    ) : this(parent, containerWidth, containerHeight) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(itemView, layoutId)
    }
}