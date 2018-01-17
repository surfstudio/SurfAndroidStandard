package ru.surfstudio.android.core.app.interactor.common.network.parse;

import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.interactor.common.network.parse.safe.SafeConverter;
import rx.functions.Func1;

/**
 * предоставляет доступ к безопасным парсерам, которые предназначены для правильного парсинга ошибочных
 * ответов сервера
 */
@PerApplication
public class SafeConverterFactory {

    private Map<Class, Func1<TypeToken, SafeConverter>> safeConverterCreators = new HashMap<>();
    private Map<Class, SafeConverter> initializedSafeConverters = new HashMap<>();

    @Inject
    public SafeConverterFactory() {
        //inject constructor
    }

    @Nullable
    public <T> SafeConverter<T> getSafeConverter(TypeToken<T> type) {
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

    public void putSafeConvertor(@NotNull Class clazz, @NotNull Func1<TypeToken, SafeConverter> converter) {
        safeConverterCreators.put(clazz, converter);
    }

    @Nullable
    private <T> SafeConverter<T> tryCreateSafeConverter(TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        Func1<TypeToken, SafeConverter> safeConverterCreator = safeConverterCreators.get(rawType);
        if (safeConverterCreator != null) {
            return safeConverterCreator.call(type);
        }
        return null;
    }


}
