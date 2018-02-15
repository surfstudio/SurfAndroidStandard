package ru.surfstudio.android.converter.gson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.surfstudio.android.converter.gson.safe.SafeConverter;
import ru.surfstudio.android.dagger.scope.PerApplication;

/**
 * предоставляет доступ к безопасным парсерам, которые предназначены для правильного парсинга ошибочных
 * ответов сервера
 */
@PerApplication
public class SafeConverterFactory {

    private interface Function<T, R> {
        R apply(T value);
    }

    private Map<Class, Function<TypeToken, SafeConverter>> safeConverterCreators = new HashMap<>();
    private Map<Class, SafeConverter> initializedSafeConverters = new HashMap<>();

    @Inject
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


}
