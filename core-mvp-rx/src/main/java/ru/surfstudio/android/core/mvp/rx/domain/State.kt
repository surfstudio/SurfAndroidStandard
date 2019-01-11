package ru.surfstudio.android.core.mvp.rx.domain

import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.lang.IllegalArgumentException

/**
 * Менеджер состояний для [EditText] с меняющимся текстом
 *
 * Содержит в себе [State] и [Action] для хранения и обработки ввода текста
 */
class State<T> : Relation<T, StateSource, StateTarget> {

    private val action = Action<T>()
    private val command = Command<T>()

    override val value: T get() =  action.value //todo thread unsafe

    override fun getSourceConsumer(source: StateSource): Consumer<T> =
            when (source) {
                is VIEW -> action.getSourceConsumer(source)
                is PRESENTER -> command.getSourceConsumer(source)
                else -> throw IllegalArgumentException("Illegal relationEntity $source")
            }

    override fun getSourceObservable(source: StateSource): Observable<T> =
            when (source) {
                is VIEW -> command.getSourceObservable(PRESENTER)
                is PRESENTER -> action.getSourceObservable(VIEW)
                else -> throw IllegalArgumentException("Illegal relationEntity $source")
            }
    override fun getTargetConsumer(target: StateTarget): Consumer<T> =
            when (target) {
                is PRESENTER -> command.getTargetConsumer(VIEW)
                is VIEW -> action.getTargetConsumer(PRESENTER)
                else -> throw IllegalArgumentException("Illegal relationEntity $target")
            }

    override fun getTargetObservable(target: StateTarget): Observable<T> =
            when (target) {
                is PRESENTER -> action.getTargetObservable(target)
                is VIEW -> command.getTargetObservable(target)
                else -> throw IllegalArgumentException("Illegal relationEntity $target")
            }
}