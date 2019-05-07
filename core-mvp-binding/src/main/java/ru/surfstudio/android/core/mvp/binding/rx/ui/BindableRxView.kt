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

package ru.surfstudio.android.core.mvp.binding.rx.ui

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.EmptyErrorException
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.LoadableState
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 *  Интерфейс для биндинга [ScreenModel] и View(mvp)
 */
interface BindableRxView : Related<VIEW>, CoreView {

    override fun relationEntity() = VIEW

    /**
     * [Observable] с ошибкой для отображения на View
     *
     * В случае, когда ошибки не возникает, эмитится [EmptyErrorException]
     */
    val <T> LoadableState<T>.appearedError: Observable<Throwable>
        get() = Observable
                .combineLatest(
                        isLoading.observable,
                        error.observable,
                        BiFunction { p1: Boolean, p2: Throwable -> Pair(p1, p2) }
                )
                .filter { !it.first }
                .map { it.second }
                .distinctUntilChanged()

    /**
     * [Observable] с флагом, показывающим, следует ли показывать лоадер в зависимости от нахождения данных в LoadableState
     */
    val <T> LoadableState<T>.isShowingLoader: Observable<Boolean>
        get() =
            isLoading.observable
                    .map { isLoading -> !hasValue && isLoading }
                    .distinctUntilChanged()


}