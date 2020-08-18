package ru.surfstudio.android.filestorage.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.surfstudio.android.filestorage.converter.ObjectConverter
import java.lang.reflect.Type

/**
 * Конвертер для прямого и обратного преобразования json-объекта в тип Т
 * Используется, когда пробразуемый объект имеет generic тип
 */
class JsonTypeConverter<T>(
    private val type: Type,
    private val gson: Gson = GsonBuilder().create()
) : ObjectConverter<T> {

    override fun encode(value: T): ByteArray = gson.toJson(value).toByteArray()

    override fun decode(rawValue: ByteArray): T = gson.fromJson(String(rawValue), type)
}
