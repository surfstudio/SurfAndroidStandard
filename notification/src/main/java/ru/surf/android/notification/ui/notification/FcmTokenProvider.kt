package ru.surf.android.notification.ui.notification

/**
 * Провайдер текущего токена Firebase
 */
interface FcmTokenProvider {
    fun provide(): String?
}