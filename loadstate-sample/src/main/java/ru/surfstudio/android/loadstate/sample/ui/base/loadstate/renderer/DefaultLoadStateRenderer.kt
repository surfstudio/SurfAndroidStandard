package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer

import ru.surfstudio.android.core.mvp.loadstate.renderer.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
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
                MainLoadingStatePresentation(placeHolderView))
        putPresentation(
                TransparentLoadingState::class.java,
                TransparentLoadingStatePresentation(placeHolderView))
    }

    fun configEmptyState(imageRes: Int? = null,
                         titleRes: Int? = null,
                         subtitleRes: Int? = null,
                         btnRes: Int? = null,
                         onBtnClickedListener: (() -> Unit)? = null): DefaultLoadStateRenderer {
        (getPresentation(EmptyLoadState::class.java) as EmptyLoadStatePresentation)
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