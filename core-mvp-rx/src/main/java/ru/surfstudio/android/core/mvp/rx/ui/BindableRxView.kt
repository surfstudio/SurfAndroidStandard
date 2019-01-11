package ru.surfstudio.android.core.mvp.rx.ui

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.rx.domain.*

interface BindableRxView<M : RxModel> : Related<VIEW, PRESENTER> {

    override fun source() = VIEW

    override fun target() = PRESENTER

    fun bind(sm: M)

    fun getDisposable(): CompositeDisposable

    fun Disposable.removeOnDestroy() = getDisposable().add(this)

    infix fun <T> Observable<T>.bindTo(consumer: Consumer<in T>) =
            this.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
                    .removeOnDestroy()


    infix fun <T> Observable<T>.bindTo(consumer: (T) -> Unit) =
            this.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
                    .removeOnDestroy()

    infix fun <V> Relation<V, VIEW, PRESENTER>.bindTo(consumer: (V) -> Unit) =
            this.getObservable()
                    .bindTo(consumer)
}