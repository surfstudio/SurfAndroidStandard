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

package ru.surfstudio.android.core.mvp.rx.sample.checkbox

import ru.surfstudio.android.core.mvp.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.rx.ui.ViewBinding

/**
 * Модель экрана [CheckboxActivityView]
 */
class CheckboxViewBinding : ViewBinding {

    val checkAction1 = Action<Boolean>()
    val checkAction2 = Action<Boolean>()
    val checkAction3 = Action<Boolean>()
    val sendAction = Action<Unit>()

    val count = State<Int>()

    val messageCommand = Command<String>()
}