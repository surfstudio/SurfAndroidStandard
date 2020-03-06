/*
  Copyright (c) 2020, SurfStudio LLC. Oleg Zhilo

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
package ru.surfstudio.android.recycler.decorator.easyadapter

import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.recycler.decorator.Decorator

/**
 * Extension for [Decorator.Builder] for work with EasyAdapter
 */

fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.overlay(pair: Pair<Int, D>): Decorator.Builder {
    return this.overlay(pair.first to BaseItemControllerDecoration(pair.second))
}

fun <D : BaseViewHolderDecor<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.underlay(pair: Pair<Int, D>): Decorator.Builder {
    return this.underlay(pair.first to BaseItemControllerDecoration(pair.second))
}

fun <D : BaseViewHolderOffset<out BaseItem<out RecyclerView.ViewHolder>>> Decorator.Builder.offset(pair: Pair<Int, D>): Decorator.Builder {
    return this.offset(pair.first to BaseItemControllerOffset(pair.second))
}