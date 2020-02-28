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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.list.carousel

import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.widget.ElementCoverView

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Отображает объект [Element] в [androidx.recyclerview.widget.RecyclerView]
 */
class CarouselElementController(
        val onClickListener: (element: Element) -> Unit
) : BindableItemController<Element, CarouselElementController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: Element) = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Element>(parent, R.layout.carousel_element_item_layout) {
        private lateinit var data: Element
        private val coverView: ElementCoverView

        init {
            itemView.setOnClickListener { onClickListener.invoke(data) }
            coverView = itemView.findViewById(R.id.cover_view)
        }

        override fun bind(data: Element) {
            this.data = data
            coverView.render(data.id)
        }
    }
}