package ru.surfstudio.standard.i_network.network.response

/**
 * Базовая ошибки ответа сервера
 */
interface BaseErrorObj {

    val code: String?
    val status: String?
    val message: String?
    val displayMessage: String?
}