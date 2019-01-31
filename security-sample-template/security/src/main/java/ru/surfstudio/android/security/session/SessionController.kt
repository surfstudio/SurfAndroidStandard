package ru.surfstudio.android.security.session

//TODO лицензия
interface SessionController {
    fun onSessionExpired()
    fun getSessionDurationInMillis(): Long
}