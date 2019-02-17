package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer

import ru.surfstudio.android.core.mvp.loadstate.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base.*
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.*

/**
 * Проектная реализация BaseLoadStateRenderer
 */
class DefaultLoadStateRenderer(
        placeHolderView: PlaceHolderViewContainer,
        override val defaultState: LoadStateInterface = NoneLoadState()) : BaseLoadStateRenderer() {

    init {
        putPresentation(
                NoneLoadState::class,
                NoneLoadStatePresentation(placeHolderView))
        putPresentation(
                EmptyLoadState::class,
                EmptyLoadStatePresentation(placeHolderView))
        putPresentation(
                ErrorLoadState::class,
                ErrorLoadStatePresentation(placeHolderView))
        putPresentation(
                MainLoadingState::class,
                MainLoadingStatePresentation(placeHolderView))
        putPresentation(
                TransparentLoadingState::class,
                TransparentLoadingStatePresentation(placeHolderView))
    }

    fun configEmptyState(imageRes: Int? = null,
                         titleRes: Int? = null,
                         subtitleRes: Int? = null,
                         btnRes: Int? = null,
                         onBtnClickedListener: (() -> Unit)? = null): DefaultLoadStateRenderer {
        (getPresentation(EmptyLoadState::class) as EmptyLoadStatePresentation)
                .configState(
                        imageRes,
                        titleRes,
                        subtitleRes,
                        btnRes,
                        onBtnClickedListener)
        return this
    }

    fun configErrorState(imageRes: Int? = null,
                         titleRes: Int? = null,
                         subtitleRes: Int? = null,
                         btnRes: Int? = null,
                         onBtnClickedListener: (() -> Unit)? = null): DefaultLoadStateRenderer {
        (getPresentation(ErrorLoadState::class) as ErrorLoadStatePresentation)
                .configState(
                        imageRes,
                        titleRes,
                        subtitleRes,
                        btnRes,
                        onBtnClickedListener)
        return this
    }
}