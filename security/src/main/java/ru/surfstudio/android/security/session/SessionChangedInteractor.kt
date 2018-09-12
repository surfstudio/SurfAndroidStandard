package ru.surfstudio.android.security.session

/**
 * Интерфейс, отвечающий за действия, выполняемые при сбросе сессии.
 */
interface SessionChangedInteractor {
    fun onSessionInvalid()
}