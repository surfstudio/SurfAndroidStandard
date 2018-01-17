package ru.surfstudio.android.core.app.interactor.common.network.parse.safe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Если приходит примитив, а ожидамое поле объект, то мапим его как null
 */
public class PrimitiveToNullSafeConverter<T> extends SafeConverter<T> {

    public PrimitiveToNullSafeConverter(TypeToken<T> type) {
        super(type);
    }

    @Override
    public T convert(TypeAdapterFactory typeAdapterFactory, Gson gson, JsonElement element) {
        if (element.isJsonPrimitive()) {
            return null;
        } else {
            return gson.getDelegateAdapter(typeAdapterFactory, getType()).fromJsonTree(element);
        }
    }
}
