package ru.surfstudio.android.analytics.store;


import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.surfstudio.android.analytics.Analytics;

/**
 * Хранилище доступных аналитик
 * Отправляет событие во все зарегистрированные аналитики
 */
public class AnalyticsStore implements Analytics {

    private List<Analytics> registeredList = new ArrayList<>(2);

    public AnalyticsStore(List<Analytics> analytics) {
        this.registeredList.addAll(analytics);
    }

    @Override
    public void sendEvent(String event) {
        Stream.of(registeredList).forEach(val -> val.sendEvent(event));
    }

    @Override
    public void sendEvent(String event, @NonNull Map<String, String> params) {
        Stream.of(registeredList).forEach(val -> val.sendEvent(event, params));
    }

    @Override
    public void setUserProperty(String key, String value) {
        Stream.of(registeredList).forEach(val -> val.setUserProperty(key, value));
    }
}
