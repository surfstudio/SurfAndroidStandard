package ru.surfstudio.android.analytics.event;

import java.util.Map;

/**
 * Описывает событие аналитики
 */
public interface Event {
    String key();
    Map<String, String> params();
}
