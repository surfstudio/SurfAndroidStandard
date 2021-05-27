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

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.item.BaseItem
import ru.surfstudio.android.easyadapter.item.NoDataItem
import ru.surfstudio.android.recycler.decorator.Decorator

/**
 * Wrapper to connect Decorator.ViewHolderDecor and EasyAdapter
 */
@Suppress("UNCHECKED_CAST")
class BaseItemControllerDecoration<I : BaseItem<out RecyclerView.ViewHolder>>(
    private val baseViewHolderDecor: BaseViewHolderDecor<I>
) : Decorator.ViewHolderDecor {

    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {

        val adapterPosition = recyclerView.getChildAdapterPosition(view)
        if (adapterPosition == NO_POSITION) {
            return
        }

        val adapter = recyclerView.adapter as EasyAdapter

        val baseItem = adapter.getItem(adapterPosition) as I

        if (adapter.isFirstInvisibleItemEnabled && baseItem is NoDataItem<*> && adapterPosition == 0) {
            return
        }

        baseViewHolderDecor.draw(canvas, view, recyclerView, state, baseItem as? I)
    }
}