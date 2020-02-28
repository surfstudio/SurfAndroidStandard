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

import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.stub.Stub
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.stub.toStub

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Отображает объект [Stub] в [androidx.recyclerview.widget.RecyclerView]
 */
class ElementStubController : BindableItemController<Stub, ElementStubController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(stub: Stub) = stub.id.toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Stub>(parent, R.layout.element_item_layout) {

        init {
            itemView.toStub()
        }

        override fun bind(stub: Stub) {
            //ignore
        }
    }
}