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
package ru.surfstudio.android.shared.pref.intid


/**
 * Задает int значение, Изначально создавалось для Enum
 * !!! Важно! При использовании в качестве идентификатора у значения enum
 * (по значению ordinal()), при этом этот enum сохраняется в кэш, НЕЛЬЗЯ
 * менять порядок элементов enum. Только добавлять в конец, иначе произойдет
 * рассинхронизация значения в хранилище (кэше) и фактических логических значений
 */
interface IntId {
    fun id(): Int
}
