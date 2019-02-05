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

import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 *  Модель главного экрана с примерами
 */
@PerScreen
class MainViewBinding @Inject constructor() : DialogControlViewBinding, CounterViewBinding, MainNavigationViewBinding, DoubleTextViewBinding {

    override val dialogInputAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<String>()
    override val dialogPositiveAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val dialogNegativeAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val dialogOpenAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val messageCommand = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command<String>()

    override val incAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val decAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val counterBond = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond(0)

    override val checkboxSampleActivityOpen = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val easyadapterSampleActivityOpen = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()

    override val doubleTextAction = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>()
    override val textEditBond = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond<String>()

    val sampleState = ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State<String>()
}