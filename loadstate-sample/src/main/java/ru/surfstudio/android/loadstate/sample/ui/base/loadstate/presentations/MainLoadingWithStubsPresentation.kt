package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubLoadStateController
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState
import ru.surfstudio.android.recycler.extension.add

class MainLoadingWithStubsPresentation(private val adapter: EasyAdapter) : LoadStatePresentation<MainLoadingState> {

    companion object {
        private const val STUBS_COUNT = 4
    }

    private val stubLoadStateController = StubLoadStateController()

    override fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: MainLoadingState) {
        adapter.setItems(ItemList.create()
                .add(stubLoadStateController, STUBS_COUNT))
        stubLoadStateController.loading = true
    }

    override fun hidePresentation(loadStateFrom: MainLoadingState, loadStateTo: LoadStateInterface) {
    }
}