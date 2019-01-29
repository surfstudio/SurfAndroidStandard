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

package ru.surfstudio.android.core.mvp.rx.sample

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Bond
import ru.surfstudio.android.core.mvp.rx.relation.mvp.State

/**
 *  Модель главного экрана с примерами
 */
class MainModel : ScreenModel() {

    val incAction = Action<Unit>()
    val decAction = Action<Unit>()
    val doubleTextAction = Action<Unit>()
    val checkboxSampleActivityOpen = Action<Unit>()
    val easyadapterSampleActivityOpen = Action<Unit>()

    val textEditState = Bond<String>()
    val counterState = Bond<Int>()

    val sampleState = State<String>()
}