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
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.checkbox.CheckboxActivityRoute
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.main.EAMainActivityRoute
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import javax.inject.Inject

/**
 * Презентер главного экрана примеров
 */
@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: MainBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            textEditBond bindTo sampleState
            dialogInputAction bindTo sampleState
        }
    }
}

/**
 * Презентер с логикой задваивания  текста
 */
@PerScreen
class DoubleTextPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: DoubleTextBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            doubleTextAction bindTo { textEditBond.change { it + it } }
        }
    }
}

interface DoubleTextBindModel : BindModel {

    val doubleTextAction: Action<Unit>
    val textEditBond: Bond<String>
}

/**
 * Презентер для инеркмента и декремента значений
 */
@PerScreen
class CounterPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: CounterBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            incAction bindTo { counterBond.change { it.inc() } }
            decAction bindTo { counterBond.change { it.dec() } }
        }
    }
}

interface CounterBindModel : BindModel {

    val incAction: Action<Unit>
    val decAction: Action<Unit>
    val counterBond: Bond<Int>
}

/**
 * Презентер для навигации
 */
@PerScreen
class MainNavigationPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val bm: MainNavigationBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            checkboxSampleActivityOpen bindTo { activityNavigator.start(CheckboxActivityRoute(true)) }
            easyadapterSampleActivityOpen bindTo { activityNavigator.start(EAMainActivityRoute()) }
        }
    }
}

interface MainNavigationBindModel : BindModel {

    val checkboxSampleActivityOpen: Action<Unit>
    val easyadapterSampleActivityOpen: Action<Unit>
}

/**
 * Презентер управления [SampleDialog]
 */
@PerScreen
class DialogControlPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val dialogNavigator: DialogNavigator,
        private val bm: DialogControlBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            dialogOpenAction bindTo { dialogNavigator.show(SampleDialogRoute()) }
            dialogPositiveAction bindTo { messageCommand.accept("Dialog close by OK") }
            dialogNegativeAction bindTo { messageCommand.accept("Dialog close by not OK") }
        }
    }
}

interface DialogControlBindModel : SampleDialogBindModel {

    val dialogOpenAction: Action<Unit>
    val messageCommand: Command<String>
}