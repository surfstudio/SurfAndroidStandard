package ru.surfstudio.android.core.mvp.rx.ui.lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.rx.ui.BindableRxView

interface LdsRxView<M>
    : BindableRxView<M> where M : LdsRxModel {
    fun getLoadStateRenderer(): LoadStateRendererInterface

    fun bindLoadState(sm: M) {
        sm.loadState.getObservable().bindTo { getLoadStateRenderer().render(it) }
    }

}