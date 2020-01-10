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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.list

import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.mvp.binding.rx.interfaces.RxClickable
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

/**
 * Origin <a href="http://google.com">https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample</a>
 *
 * Отображает статический блок в [androidx.recyclerview.widget.RecyclerView]
 * Показывает вариант реализации [RxClickable]
 */
class CommercialController
    : NoDataItemController<CommercialController.Holder>(), ru.surfstudio.android.core.mvp.binding.rx.interfaces.RxClickable {

    private var publish = PublishSubject.create<Unit>()

    override fun clicks(): Observable<Unit> = publish

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.commercial_item_layout) {
        init {
            itemView.setOnClickListener { publish.onNext(Unit) }
        }
    }
}