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

abstract class BaseRxPresenter<V>(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<V>(basePresenterDependency) where V : CoreView, V : BindableRxView<RxModel> {

    abstract fun getRxModel(): RxModel

    @CallSuper
    override fun onFirstLoad() {
        view.bind(getRxModel())
    }

    @CallSuper
    override fun onLoad(viewRecreated: Boolean) {
        if (viewRecreated) view.bind(getRxModel())
    }

    protected val <T> Action<T>.observable: Observable<T> get() = relay

    fun <T> Observable<T>.applyLoadState(new: LoadStateInterface) = map {
        val sm = getRxModel()
        if (sm is LdsRxModel) sm.loadState.accept(new)
        it
    }

    fun <T> Observable<T>.applyLoadState(loadStateFromDataAction: (T) -> LoadStateInterface) = map {
        val sm = getRxModel()
        if (sm is LdsRxModel) sm.loadState.accept(loadStateFromDataAction(it))
        it
    }

    fun <T> Action<T>.subscribeIoHandleError(onNextConsumer: Consumer<T>, onError: Consumer<Throwable>? = null) {
        subscribeIoHandleError(
                observable.doOnError {
                    handleError(it)
                    onError?.accept(it)
                }.retry(), onNextConsumer::accept)
    }

    fun <T> Action<T>.subscribeIoHandleError(onNextConsumer: (T) -> Unit, onError: Consumer<Throwable>? = null) {
        subscribeIoHandleError(Consumer { onNextConsumer(it) } , onError)
    }

    fun <T> Action<T>.subscribeIoHandleError(onNextConsumer: (T) -> Unit, onError: ((Throwable) -> Unit)? = null) {
        subscribeIoHandleError(Consumer { onNextConsumer(it) } , onError)
    }

    fun <T> Action<T>.subscribeIoHandleError(onNextConsumer: Consumer<T>, onError: ((Throwable) -> Unit)? = null) {
        val errorConsumer: ConsumerSafe<Throwable>? = if (onError != null) ConsumerSafe { onError(it) } else null
        subscribeIoHandleError(onNextConsumer, errorConsumer)
    }
}