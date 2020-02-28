/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.analytics.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Вспомогательный класс для создания события аналитики
 */
public class EventData implements Event {

    public static final Predicate TRUE = () -> true;
    static final String CURRENCY_RUB = "RUB";

    // Ключ события
    private final String event;

    // Параметры события
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

    public EventData add(String key, long value) {
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

    public String getEvent() {
        return event;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public interface Predicate {
        boolean test();
    }
}
