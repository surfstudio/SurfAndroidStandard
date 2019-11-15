package ru.surfstudio.standard.i_network.network.typeadapter

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * Адаптер для [Gson] конвертирующий пустые объекты в null
 */
class EmptyToNullTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        val elementAdapter = gson.getAdapter(JsonElement::class.java)
        return EmptyCheckTypeAdapter(delegate, elementAdapter).nullSafe()
    }

    class EmptyCheckTypeAdapter<T>(private val delegate: TypeAdapter<T>,
                                   private val elementAdapter: TypeAdapter<JsonElement>) : TypeAdapter<T>() {

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: T) {
            this.delegate.write(out, value)
        }

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): T? {
            val element = elementAdapter.read(`in`)

            if (element is JsonObject) {
                if (element.entrySet().isEmpty())
                    return null
            }
            return this.delegate.fromJsonTree(element)
        }
    }
}