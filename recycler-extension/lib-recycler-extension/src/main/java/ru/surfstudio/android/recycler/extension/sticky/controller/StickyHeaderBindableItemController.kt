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
package ru.surfstudio.android.recycler.extension.sticky.controller

import ru.surfstudio.android.easyadapter.controller.BaseItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.sticky.item.StickyHeaderBindableItem

abstract class StickyHeaderBindableItemController<T, H : BindableViewHolder<T>>
    : BaseItemController<H, StickyHeaderBindableItem<T, H>>() {

    override fun bind(holder: H, item: StickyHeaderBindableItem<T, H>) {
        bind(holder, item.data)
    }

    fun bind(holder: H, data: T) {
        holder.bind(data)
    }

    override fun getItemId(item: StickyHeaderBindableItem<T, H>): String {
        return getItemId(item.data)
    }

    override fun getItemHash(item: StickyHeaderBindableItem<T, H>): String {
        return getItemHash(item.data)
    }

    fun getItemId(data: T): String {
        return BaseItemController.NO_ID.toString()
    }

    fun getItemHash(data: T): String {
        return (data?.hashCode() ?: 0).toString()
    }
}