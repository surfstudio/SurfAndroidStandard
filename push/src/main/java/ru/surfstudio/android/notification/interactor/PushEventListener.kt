package ru.surfstudio.android.notification.interactor

import android.content.Context
import android.content.Intent

/**
 * Интерфейс обработки событий пуша
 */
interface PushEventListener {

    /**
     * Вызывается когда пуш нажато (открыто)
     */
    fun pushOpenListener(context: Context, intent: Intent)

    /**
     * Вызывается когда пуш отменено
     */
    fun pushDismissListener(context: Context, intent: Intent)
}