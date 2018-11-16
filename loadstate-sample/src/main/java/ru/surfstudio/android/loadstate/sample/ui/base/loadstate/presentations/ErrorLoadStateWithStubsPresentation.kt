package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.extension.add
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.ErrorLoadState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubLoadStateController

class ErrorLoadStateWithStubsPresentation(private val adapter: EasyAdapter) : LoadStatePresentation<ErrorLoadState> {

    companion object {
        private const val STUBS_COUNT = 4
    }

    private val errorLoadStateController = StubLoadStateController()

    override fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: ErrorLoadState) {
        adapter.setItems(ItemList.create()
                .add(errorLoadStateController, STUBS_COUNT))
    }

    override fun hidePresentation(loadStateFrom: ErrorLoadState, loadStateTo: LoadStateInterface) {
    }
}