package ru.surfstudio.android.core.mvp.rx.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import java.util.concurrent.atomic.AtomicReference

/**
 * Rx-обертка над состояниен для View
 * Всегда хранит внутри последнее значение для сохранения актуального состояния View при восстановлении ЖЦ
 *
 * Отправлять события может как View, так и Presenter
 * Подписывается на события View
 * TODO state для текста c игнорированием текущего value
 */
open class State<T>(initialValue: T? = null) {

    internal val relay =
            if (initialValue != null) {
                BehaviorRelay.createDefault<T>(initialValue).toSerialized()
            } else {
                BehaviorRelay.create<T>().toSerialized()
            }

    private val cachedValue =
            if (initialValue != null) {
                AtomicReference<T?>(initialValue)
            } else {
                AtomicReference()
            }

    /**
     * [Observable] для подписки на изменение состояния
     */
    val observable = relay.asObservable()

    /**
     * Возвращает текущее значение.
     * @throws UninitializedPropertyAccessException если нет initialValue и [State] создано без него.
     */
    val value: T
        get() {
            return cachedValue.get()
                    ?: throw UninitializedPropertyAccessException("The State has no value yet. Use valueOrNull() or pass initialValue to the constructor.")
        }

    /**
     * Текущее значение, либо null.
     */
    val valueOrNull: T? get() = cachedValue.get()

    init {
        relay.subscribe { cachedValue.set(it) }
    }

    /**
     * Возвращает true только когда у [State] есть значение.
     */
    fun hasValue() = cachedValue.get() != null

    /**
     * Принятие значения нового значения и оповещение подписчиков
     */
    fun accept(newValue: T) {
        relay.accept(newValue)
    }

    /**
     * Трансформация текущего значения, и оповещение об этом слушателей
     *
     * @param transformer функция, трансформирующая значения
     */
    fun transform(transformer: (T) -> T) {
        val newValue = transformer(value)
        accept(newValue)
    }

    /**
     * Обновление подписки при изменени внутри переменной (например, при добавлении элементов списка)
     */
    fun update() {
        accept(value)
    }
}
