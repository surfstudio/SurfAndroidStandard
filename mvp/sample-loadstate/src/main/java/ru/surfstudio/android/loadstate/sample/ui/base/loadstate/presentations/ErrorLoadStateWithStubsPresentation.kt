package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubData
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubLoadStateController
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.ErrorLoadState

/**
 * Представление состояния ErrorLoadState в виде заглушек
 */
class ErrorLoadStateWithStubsPresentation(private val adapter: EasyAdapter) :
        SimpleLoadStatePresentation<ErrorLoadState>() {

    companion object {
        private const val STUBS_COUNT = 4
    }

    override fun showState(state: ErrorLoadState) {
        val stubLoadStateController =
                StubLoadStateController()
        adapter.setItems(ItemList.create().apply {
            for (i in 1..STUBS_COUNT) {
                this.add(StubData(i, false), stubLoadStateController)
            }
        })
    }
}