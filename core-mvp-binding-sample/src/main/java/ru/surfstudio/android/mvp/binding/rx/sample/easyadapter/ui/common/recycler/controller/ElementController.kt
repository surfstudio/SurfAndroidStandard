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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.recycler.controller

import android.content.res.ColorStateList
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.widget.ElementCoverView
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Отображает объект [Element] в [androidx.recyclerview.widget.RecyclerView]
 */
class ElementController(
        val onClickListener: (element: Element) -> Unit
) : BindableItemController<SelectableData<Element>, ElementController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: SelectableData<Element>) = data.data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<SelectableData<Element>>(parent, R.layout.element_item_layout) {

        private lateinit var data: Element
        private val nameTv: TextView
        private val coverView: ElementCoverView

        init {
            itemView.setOnClickListener { onClickListener.invoke(data) }
            nameTv = itemView.findViewById(R.id.name_tv)
            coverView = itemView.findViewById(R.id.cover_view)
        }

        override fun bind(data: SelectableData<Element>) {
            this.data = data.data
            nameTv.text = data.data.name
            coverView.render(data.data.id)


            val bgTint =
                    if (data.isSelected) {
                        ColorStateList.valueOf(ResourcesCompat.getColor(itemView.resources, R.color.gray_light, null))
                    } else {
                        null
                    }
            ViewCompat.setBackgroundTintList(itemView, bgTint)

        }
    }
}