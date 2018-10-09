package ru.surfstudio.android.notification.ui.notification

import android.content.Context

/**
 * Интерфейс перехватчика пушей
 */
interface PushHandler {
    fun handleMessage(context: Context, title: String, body: String, data: Map<String, String>)
}