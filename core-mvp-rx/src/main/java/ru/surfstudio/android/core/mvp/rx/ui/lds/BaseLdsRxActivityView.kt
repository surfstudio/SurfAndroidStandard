package ru.surfstudio.android.core.mvp.rx.ui.lds

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView

abstract class BaseLdsRxActivityView<M : LdsRxModel>
    : BaseRxActivityView<M>(), LdsRxView<M> {

    @CallSuper
    override fun bind(sm: M) {
        bindLoadState(sm)
    }
}