package ru.surfstudio.android.network.parse;

import android.support.annotation.Nullable;

import com.annimon.stream.function.Function;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.network.parse.safe.SafeConverter;

/**
 * предоставляет доступ к безопасным парсерам, которые предназначены для правильного парсинга ошибочных
 * ответов сервера
 */
@PerApplication
public class SafeConverterFactory {

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

    public void putSafeConvertor(@NotNull Class clazz, @NotNull Function<TypeToken, SafeConverter> converter) {
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
