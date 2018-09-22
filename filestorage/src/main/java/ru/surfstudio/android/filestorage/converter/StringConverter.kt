package ru.surfstudio.android.filestorage.converter

/**
 * Конвертер для прямого и обратного преобразовния String в массив байтов
 */
class StringConverter : ObjectConverter<String> {

    override fun encode(value: String): ByteArray = value.toByteArray()

    override fun decode(rawValue: ByteArray): String = String(rawValue)
}