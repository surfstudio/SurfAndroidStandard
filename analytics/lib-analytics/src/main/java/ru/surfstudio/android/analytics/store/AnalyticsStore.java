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
package ru.surfstudio.android.analytics.store;


import androidx.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.surfstudio.android.analytics.Analytics;

/**
 * Хранилище доступных аналитик
 * Отправляет событие во все зарегистрированные аналитики
 */
@Deprecated
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