package ru.surfstudio.android.navigation.rx.base.disposable

import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseNavigationDisposable : Disposable {
    private val isUnsubscribed = AtomicBoolean()

    override fun isDisposed(): Boolean = isUnsubscribed.get()

    override fun dispose() {
        if (isUnsubscribed.compareAndSet(false, true)) {
            onDispose()
        }
    }

    abstract fun onDispose()
}