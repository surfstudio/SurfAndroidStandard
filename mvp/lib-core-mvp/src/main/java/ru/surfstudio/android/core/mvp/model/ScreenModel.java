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
package ru.surfstudio.android.core.mvp.model;

/**
 * базовый класс модели для отрисовки состояния вью
 * ВАЖНО! модель при отрисовке должна полностью переводить вью в необходимое состояние,
 * никакой другой механизм для изменения состояния вью использовать нельзя.
 * <p>
 * разовое отображение SnackBar не относятся к состоянию
 */
public class ScreenModel {
}
