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

import ru.surfstudio.android.analytics.Analytics;

/**
 * Позволяет отправить событие аналитики
 */
@Deprecated
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
