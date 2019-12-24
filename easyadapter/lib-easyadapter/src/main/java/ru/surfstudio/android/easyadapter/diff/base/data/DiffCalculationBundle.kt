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
package ru.surfstudio.android.easyadapter.diff.base.data

import ru.surfstudio.android.easyadapter.ItemInfo
import ru.surfstudio.android.easyadapter.ItemList

/**
 * Bundle with all necessary data for diff calculating.
 *
 * @property items New [ItemList].
 * @property oldItemInfo List with previous RecyclerView adapter item list information.
 * @property oldItemInfo List with information about [items].
 */
internal data class DiffCalculationBundle(
        val items: ItemList,
        val oldItemInfo: List<ItemInfo>,
        val newItemInfo: List<ItemInfo>
)