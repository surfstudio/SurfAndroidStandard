package ru.surfstudio.standard.base_ui.loadstate.renderer

import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.standard.base_ui.loadstate.presentation.*
import ru.surfstudio.standard.base_ui.loadstate.state.*

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