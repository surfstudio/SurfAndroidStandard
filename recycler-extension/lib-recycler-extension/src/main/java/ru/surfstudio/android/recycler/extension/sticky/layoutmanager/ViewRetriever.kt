/*
    Copyright 2016 Brandon Gogetap

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
package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

/**
 * Поставщик нужного ViewHolder'а для закрепления в родительском контейнере.
 */
interface ViewRetriever {

    fun getViewHolderForPosition(
            headerPositionToShow: Int
    ): RecyclerView.ViewHolder?

    class RecyclerViewRetriever internal constructor(
            var recyclerView: RecyclerView? = null
    ) : ViewRetriever {

        private var currentViewHolder: RecyclerView.ViewHolder? = null
        private var currentViewType: Int? = 0

        init {
            this.currentViewType = -1
        }

        override fun getViewHolderForPosition(headerPositionToShow: Int): RecyclerView.ViewHolder? {
            if (currentViewType != recyclerView?.adapter?.getItemViewType(headerPositionToShow)) {
                currentViewType = recyclerView?.adapter?.getItemViewType(headerPositionToShow)
                currentViewHolder = recyclerView?.adapter?.createViewHolder(
                        recyclerView?.parent as ViewGroup, currentViewType ?: 0)
            }
            return currentViewHolder
        }
    }
}