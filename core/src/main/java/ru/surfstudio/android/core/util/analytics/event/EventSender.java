package ru.surfstudio.android.core.util.analytics.event;

import ru.surfstudio.android.core.util.analytics.Analytics;

/**
 * Позволяет отправить событие аналитики
 */
public class EventSender {

    private final Analytics analytics;
    private final Event eventData;

    public EventSender(Analytics analytics, Event data) {
        this.analytics = analytics;
        this.eventData = data;
    }

    public static EventSender create(Analytics analytics, Event eventData) {
        return new EventSender(analytics, eventData);
    }

    public void send() {
        if (eventData.params().isEmpty()) {
            analytics.sendEvent(eventData.key());
        } else {
            analytics.sendEvent(eventData.key(), eventData.params());
        }
    }
}
