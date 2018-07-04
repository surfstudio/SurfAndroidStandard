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
package ru.surfstudio.android.core.ui.navigation;

/**
 * базовый интерфейс маршрута для навигатора
 * <p>
 * маршрут ответственен за:
 * определение, какой конкретно экран (диалог) открыть
 * <p>
 * маршрут может быть ответственен за:
 * - упаковку данных в Intent (Bundle) для передачи аргументов в экран, диалог
 * - распаковку данных из Intent (Bundle) переданных в экран, диалог
 * - упаковку данных в Intent для передачи результата на предыдущий экран
 * - распаковку данных результата выполнения экрана из Intent
 */
public interface Route {

    String EXTRA_FIRST = "EXTRA_FIRST";
    String EXTRA_SECOND = "EXTRA_SECOND";
    String EXTRA_THIRD = "EXTRA_THIRD";
    String EXTRA_FOURTH = "EXTRA_FOURTH";
    String EXTRA_FIFTH = "EXTRA_FIFTH";
    String EXTRA_SIXTH = "EXTRA_SIXTH";
    String EXTRA_SEVEN = "EXTRA_SEVEN";
    String EXTRA_EIGHT = "EXTRA_EIGHT";
    String EXTRA_NINE = "EXTRA_NINE";
    String EXTRA_TEN = "EXTRA_TEN";
}
