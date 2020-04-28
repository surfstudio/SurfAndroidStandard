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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.pagination

import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.common.stub.generateStubs
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData
import javax.inject.Inject

/**
 * Модель главного экрана с примером easyadapter и пагинацией
 */
@PerScreen
class PaginationPresentationModel @Inject constructor() : BindModel {

    val reloadAction = Action<Unit>()
    val getMoreAction = Action<Unit>()
    val selectElementAction = Action<Element>()

    val showMessageCommand = Command<String>()
    val elementsState = State<DataList<SelectableData<Element>>>(DataList.empty())
    val stubsState = State(generateStubs(20))
    val loadState = State<LoadState>(None)
    val paginationState = State(PaginationState.READY)

    var hasData = false
}

sealed class LoadState : LoadStateInterface

object Loading : LoadState()
object Error : LoadState()
object Empty : LoadState()
object None : LoadState()

