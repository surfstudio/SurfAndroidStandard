package ru.surfstudio.standard.ui.placeholder.loadstate.renderer

import ru.surfstudio.android.core.mvp.loadstate.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.standard.ui.placeholder.loadstate.presentation.*
import ru.surfstudio.standard.ui.placeholder.loadstate.state.*

/**
 * Проектная реализация BaseLoadStateRenderer
 */
class DefaultLoadStateRenderer(
        placeHolderView: PlaceHolderViewContainer,
        override val defaultState: LoadStateInterface = NoneState()
) : BaseLoadStateRenderer() {
    init {
        putPresentation(NoneState::class.java, NoneLoadStatePresentation(placeHolderView))
        putPresentation(EmptyLoadState::class.java, EmptyLoadStatePresentation(placeHolderView))
        putPresentation(ErrorLoadState::class.java, ErrorLoadStatePresentation(placeHolderView))
        putPresentation(MainLoadingState::class.java, MainLoadingStatePresentation(placeHolderView))
        putPresentation(TransparentLoadingState::class.java, TransparentLoadingStatePresentation(placeHolderView))
    }
}