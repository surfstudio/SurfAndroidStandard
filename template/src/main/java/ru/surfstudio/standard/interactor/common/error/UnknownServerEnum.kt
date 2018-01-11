package ru.surfstudio.standard.interactor.common.error

/**
 * Ошибка если с сервера приходит неизвестный enum
 */

class UnknownServerEnum : IllegalArgumentException {

    constructor(value: Int) : super(ERROR_MESSAGE + value) {}

    constructor(clazz: Class<*>, value: Int) : super(ERROR_MESSAGE + value + " для класса " + clazz.name) {}

    constructor(clazz: Class<*>, value: String) : super(ERROR_MESSAGE + value + " для класса " + clazz.name) {}

    companion object {
        private val ERROR_MESSAGE = "Неизвестное значение enum: "
    }
}
