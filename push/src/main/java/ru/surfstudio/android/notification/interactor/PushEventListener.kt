package ru.surfstudio.android.notification.interactor

import android.content.Context
import android.content.Intent

/**
 * Интерфейс обработки событий пуша
 *
 * Interface for handling push events (open, dismiss)
 */
interface PushEventListener {

    /**
     * Вызывается при тапе на пуш
     *
     * Will be call on push clicked (opened)
     */
    fun pushOpenListener(context: Context, intent: Intent)

    /**
     * Вызывается когда пуш отменено
     *
     * Will be call on push dismissed
     */
    fun pushDismissListener(context: Context, intent: Intent)
}