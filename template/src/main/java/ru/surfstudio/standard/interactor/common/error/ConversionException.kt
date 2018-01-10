package ru.surfstudio.standard.interactor.common.error

/**
 * ошибка парсинга ответа
 */
class ConversionException : NetworkException {
    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(message: String) : super(message) {}
}
