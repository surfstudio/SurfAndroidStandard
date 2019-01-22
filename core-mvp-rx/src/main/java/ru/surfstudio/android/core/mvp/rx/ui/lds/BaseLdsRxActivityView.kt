package ru.surfstudio.android.core.mvp.rx.ui.lds

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

abstract class BaseLdsRxActivityView<M>
    : BaseRxActivityView<M>(), LdsRxView<M> where M: RxModel, M: HasLoadState {

    @CallSuper
    override fun bind(sm: M) {
        bindLoadState(sm)
    }
}