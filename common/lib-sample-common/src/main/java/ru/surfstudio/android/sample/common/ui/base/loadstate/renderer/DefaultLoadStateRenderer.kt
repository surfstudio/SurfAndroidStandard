package ru.surfstudio.android.sample.common.ui.base.loadstate.renderer

import ru.surfstudio.android.core.mvp.loadstate.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.sample.common.ui.base.loadstate.*
import ru.surfstudio.android.sample.common.ui.base.loadstate.presentations.*

/**
 * Project implementation of [BaseLoadStateRenderer]
 */
class DefaultLoadStateRenderer(
        placeHolderView: PlaceHolderViewContainer,
        override val defaultState: LoadStateInterface = NoneLoadState()
) : BaseLoadStateRenderer() {

    init {
        putPresentation(
                NoneLoadState::class.java,
                NoneLoadStatePresentation(placeHolderView))
        putPresentation(
                EmptyLoadState::class.java,
                EmptyLoadStatePresentation(placeHolderView))
        putPresentation(
                ErrorLoadState::class.java,
                ErrorLoadStatePresentation(placeHolderView))
        putPresentation(
                MainLoadingState::class.java,
                MainLoadingLoadStatePresentation(placeHolderView))
    }

    fun configEmptyState(
            imageRes: Int? = null,
            titleRes: Int? = null,
            subtitleRes: Int? = null,
            btnRes: Int? = null,
            onBtnClickedListener: (() -> Unit)? = null
    ): DefaultLoadStateRenderer {
        (getPresentation(EmptyLoadState::class.java) as EmptyLoadStatePresentation)
                .configState(
                        imageRes,
                        titleRes,
                        subtitleRes,
                        btnRes,
                        onBtnClickedListener)
        return this
    }

    fun configErrorState(
            imageRes: Int? = null,
            titleRes: Int? = null,
            subtitleRes: Int? = null,
            btnRes: Int? = null,
            onBtnClickedListener: (() -> Unit)? = null
    ): DefaultLoadStateRenderer {
        (getPresentation(ErrorLoadState::class.java) as ErrorLoadStatePresentation)
                .configState(
                        imageRes,
                        titleRes,
                        subtitleRes,
                        btnRes,
                        onBtnClickedListener)
        return this
    }
}