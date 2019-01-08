package ru.surfstudio.core_mvp_rxbinding.base.ui.lds

import androidx.annotation.CallSuper
import ru.surfstudio.core_mvp_rxbinding.base.ui.BaseRxActivityView

abstract class BaseLdsRxActivityView<M : LdsRxModel>
    : BaseRxActivityView<M>(), LdsRxView<M> {

    @CallSuper
    override fun bind(sm: M) {
        bindLoadState(sm)
    }
}