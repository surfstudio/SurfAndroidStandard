package ru.surfstudio.core_mvp_rxbinding.base.ui

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.rx.extension.ConsumerSafe
import ru.surfstudio.core_mvp_rxbinding.base.domain.Action
import ru.surfstudio.core_mvp_rxbinding.base.ui.lds.LdsRxModel

abstract class BaseRxPresenter<M, V>(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<V>(basePresenterDependency)
        where M : RxModel,
              V : CoreView,
              V : BindableRxView<M> {

    abstract fun getRxModel(): M

    @CallSuper
    override fun onFirstLoad() {
        view.bind(getRxModel())
    }

    @CallSuper
    override fun onLoad(viewRecreated: Boolean) {
        if (viewRecreated) view.bind(getRxModel())
    }

    protected val <T> Action<T>.observable: Observable<T> get() = relay

    protected fun applyLoadState(new: LoadStateInterface) {
        val sm = getRxModel()
        if (sm is LdsRxModel) sm.loadState.accept(new)
    }

    protected fun <T> Observable<T>.applyLoadState(new: LoadStateInterface) = map {
        this@BaseRxPresenter.applyLoadState(new)
        it
    }

    protected fun <T> Observable<T>.applyLoadState(loadStateFromDataAction: (T) -> LoadStateInterface) = map {
        this@BaseRxPresenter.applyLoadState(loadStateFromDataAction(it))
        it
    }

    fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: Consumer<T>, onError: Consumer<Throwable>? = null) {
        subscribeIoHandleError(this
                .doOnError {
                    handleError(it)
                    onError?.accept(it)
                }
                .retry(),
                onNextConsumer::accept
        )
    }

    protected fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: (T) -> Unit, onError: Consumer<Throwable>? = null) {
        subscribeIoHandleError(Consumer { onNextConsumer(it) }, onError)
    }

    protected fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: (T) -> Unit, onError: ((Throwable) -> Unit)? = null) {
        subscribeIoHandleError(Consumer { onNextConsumer(it) }, onError)
    }

    protected fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: Consumer<T>, onError: ((Throwable) -> Unit)? = null) {
        val errorConsumer: ConsumerSafe<Throwable>? = if (onError != null) ConsumerSafe { onError(it) } else null
        subscribeIoHandleError(onNextConsumer, errorConsumer)
    }

    protected fun <T> Action<T>.subscribe(consumer: Consumer<T>) {
        subscribe(this.observable, consumer as ConsumerSafe<T>)
    }

    protected fun <T> Action<T>.subscribe(consumer: (T) -> Unit) {
        subscribe(this.observable, consumer)
    }
}