package ru.surfstudio.standard.i_network.error.exception

/**
 * Базовый класс ошибки;
 * содержит дополнительное описание к [HttpProtocolException]
 */
abstract class BaseWrappedHttpException
protected constructor(
        val displayMessage: String,
        val httpCause: HttpProtocolException
) : RuntimeException(displayMessage, httpCause) {

    override fun toString(): String {
        return "BaseWrappedHttpException(displayMessage='$displayMessage', httpCause=$httpCause)"
    }
}
