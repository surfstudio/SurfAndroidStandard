/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.mvp.widget.delegate.factory;

/**
 * Контейнер для {@link MvpWidgetDelegateFactory}
 * Позволяет заменить фабрику делегатов для всего приложения,
 * тем самым изменить логику всех виджетов
 */

public class MvpWidgetDelegateFactoryContainer {

    private static MvpWidgetDelegateFactory factory = new DefaultMvpWidgetDelegateFactory();

    public static MvpWidgetDelegateFactory get() {
        return factory;
    }

    public static void set(MvpWidgetDelegateFactory factory) {
        MvpWidgetDelegateFactoryContainer.factory = factory;
    }
}
