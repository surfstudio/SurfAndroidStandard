package ru.surfstudio.android.mvp.dialog.sample.domain

import java.io.Serializable

/**
 * Данные, для изменения которых открывается диалог
 */
data class SampleData(var value: Int = 0) : Serializable {
    override fun toString(): String = value.toString()

    fun increment() {
        value++
    }

    fun decrement() {
        value--
    }
}