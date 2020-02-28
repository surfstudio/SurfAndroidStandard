package ru.surfstudio.android.easyadapter.sample.domain

import java.io.Serializable

data class FirstData(var intValue: Int = 0) : Serializable {

    override fun toString(): String = intValue.toString()

    fun increment() {
        intValue++
    }

    fun decrement() {
        intValue--
    }
}