package ru.surfstudio.standard.i_network.error.exception

/**
 * Базовый класс ошибки;
 * содержит дополнительное описание к [HttpProtocolException]
 * или в ином случае
 */
abstract class BaseWrappedHttpException
protected constructor(
        localizedMessage: String,
        val httpCause: HttpProtocolException
) : RuntimeException(localizedMessage, httpCause)
