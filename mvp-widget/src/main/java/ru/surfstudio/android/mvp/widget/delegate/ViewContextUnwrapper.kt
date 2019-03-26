package ru.surfstudio.android.mvp.widget.delegate

import android.content.Context
import android.view.ContextThemeWrapper


/**
 * Helps to avoid problems with ContextThemeWrapper
 * @see <a href="https://clck.ru/FRZrW">ContextThemeWrapper problem</a>
 */
inline fun <reified T> unwrapContext(context: Context): T {
    var unwrappedContext = context
    while (unwrappedContext !is T && unwrappedContext is ContextThemeWrapper) {
        unwrappedContext = unwrappedContext.baseContext
    }
    return unwrappedContext as T
}