package ru.surfstudio.android.notification.interactor

/**
 * Singleton содержащий интерфейс обработки пуш событий
 * нужно инициализировать нужным обработчиком
 */
object PushClickProvider {

    var pushEventListener: PushEventListener? = null
}