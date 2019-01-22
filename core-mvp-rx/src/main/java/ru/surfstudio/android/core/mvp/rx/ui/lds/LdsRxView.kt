package ru.surfstudio.android.core.mvp.rx.ui.lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.rx.ui.BindableRxView
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

interface LdsRxView<M>
    : BindableRxView<M>
        where M : RxModel,
              M : HasLoadState {
    fun getLoadStateRenderer(): LoadStateRendererInterface

    fun bindLoadState(sm: M) {
        sm.loadState.observable.bindTo { getLoadStateRenderer().render(it) }
    }

}