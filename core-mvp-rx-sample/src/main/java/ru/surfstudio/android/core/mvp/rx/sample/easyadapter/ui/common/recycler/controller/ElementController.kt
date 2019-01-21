/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.widget.ElementCoverView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.easyadapter.sample.domain.Element
import ru.surfstudio.sample.R


class ElementController(
        val onClickListener: (element: Element) -> Unit
) : BindableItemController<Element, ElementController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: Element) = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Element>(parent, R.layout.element_item_layout) {
        private lateinit var data: Element
        private val nameTv: TextView
        private val coverView: ElementCoverView

        init {
            itemView.setOnClickListener { onClickListener.invoke(data) }
            nameTv = itemView.findViewById(R.id.name_tv)
            coverView = itemView.findViewById(R.id.cover_view)
        }

        override fun bind(data: Element) {
            this.data = data
            nameTv.text = data.name
            coverView.render(data.id)
        }
    }
}