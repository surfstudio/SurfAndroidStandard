package ru.surfstudio.android.notification.ui.notification

/**
 * Провайдер текущего токена Firebase
 */
interface FcmTokenProvider {
    fun provide(): String?
}