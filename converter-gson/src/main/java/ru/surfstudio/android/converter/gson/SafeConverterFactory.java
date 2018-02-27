package ru.surfstudio.android.converter.gson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import ru.surfstudio.android.converter.gson.safe.SafeConverter;

/**
 * предоставляет доступ к безопасным парсерам, которые предназначены для правильного парсинга ошибочных
 * ответов сервера
 */
public class SafeConverterFactory {

    private Map<Class, Function<TypeToken, SafeConverter>> safeConverterCreators = new HashMap<>();
    private Map<Class, SafeConverter> initializedSafeConverters = new HashMap<>();
    public SafeConverterFactory() {
        //inject constructor
    }

    @Nullable
    <T> SafeConverter<T> getSafeConverter(TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        SafeConverter<T> result = initializedSafeConverters.get(rawType);
        if (result == null) {
            result = tryCreateSafeConverter(type);
            if (result != null) {
                initializedSafeConverters.put(rawType, result);
            }
        }
        return result;
    }

    public void putSafeConvertor(@NonNull Class clazz, @NonNull Function<TypeToken, SafeConverter> converter) {
        safeConverterCreators.put(clazz, converter);
    }

    @Nullable
    private <T> SafeConverter<T> tryCreateSafeConverter(TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        Function<TypeToken, SafeConverter> safeConverterCreator = safeConverterCreators.get(rawType);
        if (safeConverterCreator != null) {
            return safeConverterCreator.apply(type);
        }
        return null;
    }

    private interface Function<T, R> {
        R apply(T value);
    }
}
