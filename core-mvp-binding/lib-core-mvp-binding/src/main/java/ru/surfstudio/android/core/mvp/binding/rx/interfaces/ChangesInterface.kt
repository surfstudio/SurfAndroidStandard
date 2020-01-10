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

package ru.surfstudio.android.core.mvp.binding.rx.interfaces

import io.reactivex.Observable

/**
 *  Набор базовых интерфейсов для создаия кастомных вью и `ItemController`
 */


/**
 *  Интерфес для объекта с изменяющеся моделью
 */
interface RxChangesModel<T> {

    fun modelChanges(): Observable<T>
}

/**
 *  Интерфес для объекта с изменяющимся состояниянием выбрано/невыбрано
 */
interface RxChangedChecked {

    fun checkedChanges(): Observable<Boolean>
}

/**
 *  Интерфес для объекта который принимает клики.
 *  Используется там, где невозможно использовать [com.jakewharton.rxbinding2.view.RxView.clicks]
 */
interface RxClickable {

    fun clicks(): Observable<Unit>
}