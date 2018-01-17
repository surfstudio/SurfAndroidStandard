package ru.surfstudio.android.core.app.interactor.common.network.parse.safe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;


public abstract class SafeConverter<T> {

    private TypeToken<T> type;

    public SafeConverter(TypeToken<T> type) {
        this.type = type;
    }

    public TypeToken<T> getType() {
        return type;
    }

    public abstract T convert(TypeAdapterFactory typeAdapterFactory, Gson gson, JsonElement element);

}
