package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer

import ru.surfstudio.android.core.mvp.loadstate.BaseLoadStateRenderer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.ErrorLoadStateWithStubsPresentation
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.MainLoadingWithStubsPresentation
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base.NoneLoadStatePresentation
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.ErrorLoadState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState

class LoadStateWithStubsRenderer(
        adapter: EasyAdapter,
        override val defaultState: LoadStateInterface = NoneLoadState()) : BaseLoadStateRenderer() {

    init {
        putPresentation(NoneLoadState::class.java, NoneLoadStatePresentation())
        putPresentation(ErrorLoadState::class.java, ErrorLoadStateWithStubsPresentation(adapter))
        putPresentation(MainLoadingState::class.java, MainLoadingWithStubsPresentation(adapter))
    }
}