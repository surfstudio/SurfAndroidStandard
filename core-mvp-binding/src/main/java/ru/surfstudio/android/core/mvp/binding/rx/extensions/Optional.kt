package ru.surfstudio.android.core.mvp.binding.rx.extensions

import java.io.Serializable

/**
 * Эрзац [java.util.Optional]
 */
open class Optional<T>
internal constructor(
        private val value: T?
) : Serializable {

    /**
     * @see [java.util.Optional.get]
     */
    fun get(): T {
        if (value == null) throw NoSuchElementException("No value present")
        return value
    }

    /**
     * Для клиентов не поддерживающих non-null подход
     */
    fun getOrNull(): T? = value

    /**
     * @see [java.util.Optional.isPresent]
     */
    fun isPresent(): Boolean {
        return value != null
    }

    /**
     * @see [java.util.Optional.ifPresent]
     */
    fun ifPresent(consumer: (T) -> Unit) {
        if (value != null) consumer(value)
    }

    /**
     * @see [java.util.Optional.empty]
     */
    object EMPTY : Optional<Any>(null)

    companion object {

        /**
         * @see [java.util.Optional.of]
         */
        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }

        /**
         * @see [java.util.Optional.empty]
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> empty(): Optional<T> {
            return EMPTY as Optional<T>
        }
    }
}