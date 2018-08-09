package ru.surfstudio.android.easyadapter.sample.domain

import java.io.Serializable

data class SampleData(var value: Int = 0) : Serializable {

    override fun toString(): String = value.toString()

    fun increment() {
        value++
    }

    fun decrement() {
        value--
    }
}