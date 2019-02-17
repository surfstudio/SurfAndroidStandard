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
package ru.surfstudio.android.core.ui.state;

/**
 * Интерфейс для всех ScreenState
 * предоставляет текущее состояние экрана
 */

public interface ScreenState {

    /**
     * @return пересоздана ли иерархия вью после смены конфигурации
     * если экран быль только восстановлен с диска, то метод вернет false
     */
    boolean isViewRecreated();

    /**
     * @return пересоздана ли обьект экрана после смены конфигурации
     * если экран быль только восстановлен с диска, то метод вернет false
     */
    boolean isScreenRecreated();

    /**
     * @return уничтожен ли экран полностью и не будет восстановлен
     */
    boolean isCompletelyDestroyed();

    /**
     * @return восстановлен ли экран с диска
     * после смены конфигурации метод вернет true если раньше он был восстановлен
     * с диска
     */
    boolean isRestoredFromDisk();

    /**
     * @return восстановлен ли экран с диска
     * после смены конфигурации метод вернет false если раньше он был восстановлен
     * с диска
     */
    boolean isRestoredFromDiskJustNow();

    LifecycleStage getLifecycleStage();
}
