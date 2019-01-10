package ru.surfstudio.android.core.mvp.rx.ui

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import ru.surfstudio.android.core.mvp.activity.CoreActivityView

abstract class BaseRxActivityView<M : RxModel> : CoreActivityView(), BindableRxView<M> {
    private val viewDisposable = CompositeDisposable()

    @CallSuper
    override fun onDestroy() {
        viewDisposable.clear()
        super.onDestroy()
    }

    override fun getDisposable() = viewDisposable
}