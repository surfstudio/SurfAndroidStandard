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
package ru.surfstudio.android.analyticsv2.core

/**
 * Представление действия в аналитике.
 * "Действие" идет без привязки к реализации конкретного сервиса аналитики,
 * т.е реализующая сущность может выполнить любое действия на конкретном api (Firebase, Flurry, AppMetrica...),
 * например, отправку события или установку свойств пользователя (setUserProperty)
 */
interface AnalyticAction