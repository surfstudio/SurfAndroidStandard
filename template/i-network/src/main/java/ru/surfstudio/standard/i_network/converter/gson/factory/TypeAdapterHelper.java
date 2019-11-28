package ru.surfstudio.standard.i_network.converter.gson.factory;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import io.reactivex.Completable;
import ru.surfstudio.android.logger.Logger;

public class TypeAdapterHelper {

    /**
     * Парсинг элементов с учётом указания Completable в кач-ве результата;
     * применяется на первом зарегистрированном {@link TypeAdapterFactory}
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseElement(
            JsonReader reader,
            JsonElement jsonElement,
            TypeToken<T> type,
            TypeAdapter<T> delegate
    ) {
        if (Completable.class.isAssignableFrom(type.getRawType())) {
            consumeDocument(reader);
            return (T) Completable.complete();
        } else {
            return delegate.fromJsonTree(jsonElement);
        }
    }

    /**
     * Дочитать документ до конца
     * @param reader из изначального TypeAdapterFactory
     */
    private static void consumeDocument(JsonReader reader) {
        JsonToken token = null;
        while (token != JsonToken.END_DOCUMENT) {
            try {
                token = reader.peek();
                if (reader.hasNext()) {
                    reader.skipValue();
                }
            } catch (IOException e) {
                Logger.e("An IOException occurred during read from reader " + reader + ": " + e);
            }
        }
    }
}