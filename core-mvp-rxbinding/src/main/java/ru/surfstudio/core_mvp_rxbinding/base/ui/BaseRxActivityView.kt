package ru.surfstudio.core_mvp_rxbinding.base.ui

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.model.ScreenModel

abstract class BaseRxActivityView<in M : RxModel> : CoreActivityView(), BindableRxView<M> {
    private val viewDisposable = CompositeDisposable()

    @CallSuper
    override fun onDestroy() {
        viewDisposable.dispose()
        super.onDestroy()
    }

    override fun Disposable.removeOnDestroy() { viewDisposable.add(this) }
}