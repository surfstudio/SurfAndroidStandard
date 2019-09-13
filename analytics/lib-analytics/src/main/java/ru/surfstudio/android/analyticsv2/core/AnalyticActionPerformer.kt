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
 * Осуществляет выполнение действия в аналитике
 */
interface AnalyticActionPerformer<Action: AnalyticAction> {
    /**
     * Может ли выполнитель обработать действие?
     * @return true если может
     */
    fun canHandle(action: AnalyticAction) : Boolean
    /**
     * Выполнить действие
     */
    fun perform(action: Action)
}