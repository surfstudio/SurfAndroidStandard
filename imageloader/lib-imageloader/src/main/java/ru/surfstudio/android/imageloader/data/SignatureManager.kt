package ru.surfstudio.android.imageloader.data

/**
 * Обертка для сигнатур, т.е. признака, по которому перезагружается изображение.
 *
 * Signature-wrapper for RequestOptions#signature()
 */
class SignatureManager {
    var signature: Any? = null

    val hasSignature get() = signature != null
}