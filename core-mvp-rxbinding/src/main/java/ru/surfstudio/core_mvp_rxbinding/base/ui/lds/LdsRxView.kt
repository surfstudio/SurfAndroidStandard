package ru.surfstudio.core_mvp_rxbinding.base.ui.lds

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.core_mvp_rxbinding.base.ui.BindableRxView

interface LdsRxView<M>
    : BindableRxView<M> where M : LdsRxModel {
    fun getLoadStateRenderer(): LoadStateRendererInterface

    fun bindLoadState(sm: M) {
        sm.loadState.observable.bindTo { getLoadStateRenderer().render(it) }
    }

}