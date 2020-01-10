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

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.recycler.animator.StandardItemAnimator
import ru.surfstudio.easyadapter.sample.domain.Carousel

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Отображает объект [Carousel] в [androidx.recyclerview.widget.RecyclerView]
 */
class CarouselController(
        val onElementClickListener: (element: Element) -> Unit,
        val onShowAllClickListener: (carousel: Carousel) -> Unit
) : BindableItemController<Carousel, CarouselController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: Carousel) = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Carousel>(parent, R.layout.carousel_item_layout) {
        private lateinit var data: Carousel
        private val titleTv: TextView
        private val adapter: EasyAdapter = EasyAdapter()
        private val carouselElementController = CarouselElementController(onElementClickListener)

        init {
            val allBtn = itemView.findViewById<View>(R.id.carousel_all_btn)
            allBtn.setOnClickListener { onShowAllClickListener.invoke(data) }
            titleTv = itemView.findViewById<TextView>(R.id.carousel_title_tv)

            val recyclerView = itemView.findViewById<RecyclerView>(R.id.carousel_recycler)
            val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val itemAnimator = StandardItemAnimator()
            GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.itemAnimator = itemAnimator
        }

        override fun bind(data: Carousel) {
            this.data = data
            titleTv.text = data.name
            adapter.setData(data.elements, carouselElementController)
        }
    }
}