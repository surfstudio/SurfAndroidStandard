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
package ru.surfstudio.android.recycler.extension.sticky

import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.recycler.extension.sticky.layoutmanager.StickyHeaderHandler
import ru.surfstudio.android.recycler.extension.sticky.layoutmanager.StickyLayoutManager

/**
 * EasyAdapter, упрощающий работу со Sticky Items. Поддерживается два типа Sticky Items:
 * * Header - прикрепляется к верхнему краю экрана;
 * * Footer - прикрепляется к нижнему краю экрана.
 *
 * Допустимо в одном адаптере использовать сразу оба типа StickyItems.
 *
 * При использовании [StickyEasyAdapter] требуется просто передать экземпляр [RecyclerView], к
 * которому требуется применить адаптер. Вручную устанавливать данному экземпляру [RecyclerView]
 * адаптеры и LayoutManager'ы не разрешается, так как это нарушит работу Sticky Item'ов.
 *
 * @param recyclerView экземпляр [RecyclerView] для поддержки в нём Sticky Items
 * @param isVisibleFirstFooterAtLaunch требуется ли сразу отображать первый футер по нижней границе
 * экрана даже в том случае, если до него список ещё не доскроллен.
 */
class StickyEasyAdapter(
        recyclerView: RecyclerView,
        private val isVisibleFirstFooterAtLaunch: Boolean = false
) : EasyAdapter() {

    val stickyLayoutManager: StickyLayoutManager = StickyLayoutManager(recyclerView.context,
            object : StickyHeaderHandler {
                override fun getAdapterData(): List<*> {
                    return items
                }
            },
            isVisibleFirstFooterAtLaunch
    )

    init {
        recyclerView.layoutManager = stickyLayoutManager
        recyclerView.adapter = this
    }
}