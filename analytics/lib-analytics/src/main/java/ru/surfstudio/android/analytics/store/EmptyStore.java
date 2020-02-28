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

import java.util.Map;

import ru.surfstudio.android.analytics.Analytics;

/**
 * Делает ничего.
 * При использовании этого класса события не отправляются ни в какой сервис аналитики
 */
@Deprecated
public class EmptyStore implements Analytics {

    @Override
    public void sendEvent(String event) {
        //nothing
    }

    @Override
    public void sendEvent(String event, @NonNull Map<String, String> params) {
        //nothing
    }

    @Override
    public void setUserProperty(String key, String value) {
        //nothing
    }
}

