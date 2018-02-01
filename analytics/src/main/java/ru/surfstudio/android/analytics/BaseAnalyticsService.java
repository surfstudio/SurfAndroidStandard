package ru.surfstudio.android.analytics;

import ru.surfstudio.android.analytics.event.Event;
import ru.surfstudio.android.analytics.event.EventSender;

/**
 * Базовый сервис аналитики
 */
public class BaseAnalyticsService {

    protected Analytics apiStore;

    public BaseAnalyticsService(Analytics apiStore) {
        this.apiStore = apiStore;
    }

    public void sendEvent(Event event) {
        create(event).send();
    }

    protected EventSender create(Event event) {
        return EventSender.create(apiStore, event);
    }
}
