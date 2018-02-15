package ru.surfstudio.android.core.util

import android.content.Context
import android.support.annotation.StringRes
import javax.inject.Inject

/**
 * Класс, предоставляющий строки из ресурсов по id
 */
class StringsProvider @Inject constructor(var context: Context) {

    fun getString(@StringRes id: Int): String {
        return context.resources.getString(id)
    }
}