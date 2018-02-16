package ru.surfstudio.android.core.util

import android.content.Context
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Класс, предоставляющий строки из ресурсов по id
 */
@PerApplication
class StringsProvider @Inject constructor(var context: Context) {

    fun getString(@StringRes id: Int, vararg args: Any): String {
        return if (args.isEmpty()) {
            context.resources.getString(id)
        } else {
            context.resources.getString(id, *args)
        }
    }

    fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg args: Any): String {
        return if (args.isEmpty()) {
            context.resources.getQuantityString(id, quantity)
        } else {
            context.resources.getQuantityString(id, quantity, *args)
        }
    }
}