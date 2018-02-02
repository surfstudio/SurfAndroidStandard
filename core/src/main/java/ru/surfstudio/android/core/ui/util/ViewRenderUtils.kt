package ru.surfstudio.android.core.ui.util

import android.view.View

/**
 * Extention метод, который запускает action если данные изменились
 */
fun <T> View.actionIfChanged(data: T, action: (data: T) -> Unit) {
    val hash = data?.hashCode()
    if (this.tag != hash) {
        action.invoke(data)
        this.tag = hash
    }
}