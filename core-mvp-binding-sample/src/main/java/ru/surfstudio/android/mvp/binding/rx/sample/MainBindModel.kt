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

package ru.surfstudio.android.mvp.binding.rx.sample

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 *  Модель главного экрана с примерами
 */
@PerScreen
class MainBindModel @Inject constructor()
    : DialogControlBindModel,
        CounterBindModel,
        MainNavigationBindModel,
        DoubleTextBindModel {

    override val dialogInputAction = Action<String>()
    override val dialogPositiveAction = Action<Unit>()
    override val dialogNegativeAction = Action<Unit>()
    override val dialogOpenAction = Action<Unit>()
    override val messageCommand = Command<String>()

    override val incAction = Action<Unit>()
    override val decAction = Action<Unit>()
    override val counterBond = Bond(0)

    override val checkboxSampleActivityOpen = Action<Unit>()
    override val easyadapterSampleActivityOpen = Action<Unit>()

    override val doubleTextAction = Action<Unit>()
    override val textEditBond = Bond<String>()

    val sampleState = State<String>()
}