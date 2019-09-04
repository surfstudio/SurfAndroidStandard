/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.recycler.extension.sticky.item

import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.recycler.extension.sticky.controller.StickyFooterBindableItemController

/**
 * Контейнер для элемента списка со свойствами Sticky Footer (прилипает к нижней части списка).
 *
 * @param data данные, необходимые для отрисовки элемента списка
 * @param itemControllerHeader контроллер, описывающий внешний вид и поведение элемента списка
 */
class StickyFooterBindableItem<T, H : BindableViewHolder<T>>(
        val data: T,
        itemControllerHeader: StickyFooterBindableItemController<T, H>
) : BaseItem<H>(itemControllerHeader), StickyFooter