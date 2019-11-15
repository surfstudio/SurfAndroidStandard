package ru.surfstudio.standard.i_network.converter.gson

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.lang.reflect.Type
import java.util.*

/**
 * Вспомогательный класс для работы с json
 * посредством [Gson]
 */
object GsonHelper {

    /**
     * Преобразует строку [jsonString] в инстанс указанного типа [T],
     * информация о котором содержится в [classOfT],
     * используя [gson]
     */
    fun <T> fromJsonObjectString(gson: Gson, jsonString: String?, classOfT: Class<T>): T? =
            fromJsonObjectString<T>(gson, jsonString, classOfT as Type)

    /**
     * Преобразует строку [jsonString] в инстанс указанного типа [T],
     * информация о котором содержится в [typeToken],
     * используя [gson]
     */
    fun <T> fromJsonObjectString(gson: Gson, jsonString: String?, typeToken: TypeToken<T>): T? =
            fromJsonObjectString<T>(gson, jsonString, typeToken.type)


    /**
     * Преобразует строку [jsonString] в инстанс указанного типа [T],
     * информация о котором содержится в [type],
     * используя [gson]
     */
    fun <T> fromJsonObjectString(gson: Gson, jsonString: String?, type: Type): T? =
            try {
                gson.fromJson<T>(jsonString, type)
            } catch (e: JsonParseException) {
                Logger.e("An exception occurred during fromJson: $e")
                null
            }

    /**
     * Преобразует строку [jsonString] в список сущностей указанного типа [T],
     * информация о котором содержится в [type],
     * используя [gson]
     */
    inline fun <reified T> fromJsonArrayString(gson: Gson, jsonString: String?, type: Class<Array<T>>): List<T> {
        var array: Array<T>
        try {
            array = gson.fromJson(jsonString, type)
        } catch (e: JsonParseException) {
            Logger.e("An exception occurred during fromJson: $e")
            array = arrayOf()
        }
        return ArrayList(listOf(*array))
    }

    /**
     * Преобразует объект [obj] указанного типа [T]
     * в строку, используя [gson]
     */
    fun <T> toJsonString(gson: Gson, obj: T): String =
            try {
                gson.toJson(obj)
            } catch (e: JsonParseException) {
                Logger.e("An exception occurred during toJson: $e")
                EMPTY_STRING
            }

    /**
     * Преобразует коллекцию объектов [listOfObjects] указанного типа [T]
     * в маппинг: объект - json-строка, используя [gson]
     */
    fun <T> toJsonStringMap(gson: Gson, listOfObjects: Collection<T>?): Map<T, String> {
        val result = LinkedHashMap<T, String>()
        if (listOfObjects != null) {
            for (o in listOfObjects) {
                result[o] = toJsonString(gson, o)
            }
        }
        return result
    }
}