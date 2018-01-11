package ru.surfstudio.standard.interactor.common.error

/**
 * базовый класс для всех ошибок, возникающих при работе с сервером
 */
abstract class NetworkException : RuntimeException {
    constructor() {}

    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
