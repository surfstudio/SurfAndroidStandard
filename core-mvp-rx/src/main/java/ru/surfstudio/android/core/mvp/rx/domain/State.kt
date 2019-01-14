package ru.surfstudio.android.core.mvp.rx.domain

import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.atomic.AtomicReference

/**
 * Менеджер состояний для [EditText] с меняющимся текстом
 *
 * Содержит в себе [State] и [Action] для хранения и обработки ввода текста
 */
class State<T> : Relation<T, StateSource, StateTarget> {

    override val hasValue: Boolean get() = cachedValue.get() != null

    private val action = Action<T>()
    private val command = Command<T>()

    private val cachedValue = AtomicReference<T>()
    override val value: T get() = cachedValue.get()

    override fun getConsumer(source: StateSource): Consumer<T> =
            when (source) {
                is VIEW -> action.getConsumer(source)
                is PRESENTER -> command.getConsumer(source)
                else -> throw IllegalArgumentException("Illegal relationEntity $source")
            }

    override fun getObservable(target: StateTarget): Observable<T> =
            when (target) {
                is PRESENTER -> action.getObservable(target)
                is VIEW -> command.getObservable(target)
                else -> throw IllegalArgumentException("Illegal relationEntity $target")
            }
                    .doOnNext(cachedValue::set)
}