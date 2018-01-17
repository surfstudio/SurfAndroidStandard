package ru.surfstudio.android.core.app.interactor.common.network.parse.safe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;


/**
 * если вместо ожидаемого объекта находится JsonArray - возвращает null
 *
 * @param <T>
 */
public class ArrayToNullSafeConverter<T> extends SafeConverter<T> {

    public ArrayToNullSafeConverter(TypeToken<T> type) {
        super(type);
    }

    @Override
    public T convert(TypeAdapterFactory typeAdapterFactory, Gson gson, JsonElement element) {
        if (element.isJsonArray()) {
            return null;
        } else {
            return gson.getDelegateAdapter(typeAdapterFactory, getType()).fromJsonTree(element);
        }
    }
}
