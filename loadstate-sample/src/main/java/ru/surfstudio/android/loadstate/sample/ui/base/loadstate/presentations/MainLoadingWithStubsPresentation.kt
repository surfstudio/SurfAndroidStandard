package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubData
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubLoadStateController
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState

/**
 * Представление состояния MainLoading в виде мерзающих заглушек
 */
class MainLoadingWithStubsPresentation(private val adapter: EasyAdapter) :
        SimpleLoadStatePresentation<MainLoadingState>() {

    companion object {
        private const val STUBS_COUNT = 4
    }

    override fun showState(state: MainLoadingState) {
        val stubLoadStateController =
                StubLoadStateController()
        adapter.setItems(ItemList.create().apply {
            for (i in 1..STUBS_COUNT) {
                this.add(StubData(i, true), stubLoadStateController)
            }
        })
    }
}