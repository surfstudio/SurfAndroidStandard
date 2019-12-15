package ru.surfstudio.android.core.mvp.binding.rx.relation.mvp

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.extensions.filterValue
import ru.surfstudio.android.core.mvp.binding.rx.relation.ValuableRelation

/**
 *  Отношение Presenter -> View
 *
 *  Эмитит единичное событие.
 *  В отличии от [Command], может сэмитить последнее значение при подписке, если оно было получено
 *  после предыдущей отписки.
 *
 *  Служит для того, чтобы оповестить View о пропущенных коммандах,
 *  которые были получены пока экран пересоздавался.
 */
class BufferedCommand<T> : ValuableRelation<T, PRESENTER, StateTarget>() {

    protected val relay = BehaviorRelay.create<Optional<T>>()

    override fun getConsumer(source: PRESENTER): Consumer<T> = Consumer { relay.accept(Optional.of(it)) }

    override fun getObservable(target: StateTarget): Observable<T> = relay
            .doOnDispose { relay.accept(Optional.empty()) }
            .filterValue()

    override val hasValue: Boolean get() = relay.value?.getOrNull() != null

    override val internalValue: T get() = relay.value?.getOrNull() ?: throw NoSuchElementException()

    /**
     * Отправляет повторно последний объект. Используется в тех случаях, когда не удается работать без
     * mutable объектов
     */
    fun update() = relay.accept(Optional.of(internalValue))
}