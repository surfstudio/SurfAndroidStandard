package ru.surfstudio.android.core.util.analytics.event;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Вспомагательный класс для создания события аналитики
 */
public class EventData implements Event {

    public static final Predicate TRUE = () -> true;
    static final String CURRENCY_RUB = "RUB";
    /**
     * Ключ события
     */
    @Getter
    private final String event;
    /**
     * Параметры события
     */
    @Getter
    private final Map<String, String> params;

    public EventData(String event, int numberOfParams) {
        this.event = event;
        this.params = new HashMap<>(numberOfParams);
    }


    public EventData(Event event) {
        this.event = event.key();
        this.params = new HashMap<>(event.params());
    }

    public EventData(String event) {
        this(event, 4);
    }

    @Override
    public String key() {
        return event;
    }

    @Override
    public Map<String, String> params() {
        return params;
    }

    public EventData add(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public EventData add(String key, int value) {
        return add(key, String.valueOf(value));
    }

    public EventData add(String key, double value) {
        return add(key, String.valueOf(value));
    }

    public EventData addIf(String key, String value, Predicate predicate) {
        if (predicate.test()) {
            return add(key, value);
        }
        return this;
    }

    public EventData addIf(String key, boolean value, Predicate predicate) {
        return addIf(key, String.valueOf(value), predicate);
    }

    public EventData addDefaultCurrency() {
        return add("currency", CURRENCY_RUB);
    }

    public interface Predicate {
        boolean test();
    }
}
