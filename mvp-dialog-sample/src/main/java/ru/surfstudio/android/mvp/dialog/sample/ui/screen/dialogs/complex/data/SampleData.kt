package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.data

import java.io.Serializable

/**
 * Данные, для изменения которых открывается диалог
 */
data class SampleData(var value: Int = 0) : Serializable {
    override fun toString(): String = value.toString()
}