package ru.surfstudio.android.core.util.analytics;


import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Обобщенный api для работы с аналитикой
 *
 * Реализующий класс должен отправить событие при необходимости преобразовав параметры в нужный вид
 */
public interface Analytics {

    /**
     * Событие без параметров
     *
     * @param event название события
     */
    void sendEvent(String event);

    /**
     * Событие с параметрами
     *
     * @param event  название события
     * @param params параметры
     */
    void sendEvent(String event, @NonNull Map<String, String> params);

    /**
     * Установить параметр пользователя
     *
     * @param key название параметра
     * @param value значение
     */
    void setUserProperty(String key, String value);
}
