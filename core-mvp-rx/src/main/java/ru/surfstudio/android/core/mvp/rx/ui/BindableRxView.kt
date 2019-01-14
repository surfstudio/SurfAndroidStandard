package ru.surfstudio.android.core.mvp.rx.ui

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.rx.domain.Related
import ru.surfstudio.android.core.mvp.rx.domain.Relation
import ru.surfstudio.android.core.mvp.rx.domain.VIEW

interface BindableRxView<M : RxModel> : Related<VIEW> {

    override fun relationEntity() = VIEW

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

    infix fun <T> Relation<T, *, VIEW>.bindTo(consumer: (T) -> Unit) =
            this.getObservable()
                    .bindTo(consumer)

    infix fun <T> Observable<T>.bindTo(relation: Relation<T, VIEW, *>) =
            this.bindTo(relation.getConsumer())
}