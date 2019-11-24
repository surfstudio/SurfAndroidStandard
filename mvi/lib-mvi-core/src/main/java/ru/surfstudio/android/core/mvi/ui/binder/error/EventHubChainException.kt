package ru.surfstudio.android.core.mvi.ui.binder.error

/**
 * Событие ошибки в цепочке событий [EventHub]
 *
 * Оборачивается для того, чтобы RxJavaPlugins.setErrorHandler не игнорировал ошибки,
 * проглатывая и ломая цепочку событий на экране, а явно крашил бы на них приложение.
 */
class EventHubChainException(cause: Throwable) : RuntimeException(cause)