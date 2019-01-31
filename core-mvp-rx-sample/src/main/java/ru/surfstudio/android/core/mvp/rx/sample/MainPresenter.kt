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

import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Action
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Bond
import ru.surfstudio.android.core.mvp.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.rx.sample.checkbox.CheckboxActivityRoute
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.EAMainActivityRoute
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.rx.ui.ViewBinding
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import javax.inject.Inject

/**
 * Презентер главного экрана примеров
 */
@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BaseRxPresenter<MainViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var vb: MainViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(vb) {
            textEditBond bindTo sampleState
            dialogInputAction bindTo sampleState
        }
    }
}

/**
 * Презентер с логикой задавивания  текста
 */
@PerScreen
class DoubleTextPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BaseRxPresenter<DoubleTextViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var vb: DoubleTextViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(vb) {
            doubleTextAction bindTo { textEditBond.change { it + it } }
        }
    }
}

interface DoubleTextViewBinding : ViewBinding {

    val doubleTextAction: Action<Unit>
    val textEditBond: Bond<String>
}

/**
 * Презентер для инеркмента и декремента значений
 */
@PerScreen
class CounterPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BaseRxPresenter<CounterViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var vb: CounterViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(vb) {
            incAction bindTo { counterBond.change { it.inc() } }
            decAction bindTo { counterBond.change { it.dec() } }
        }
    }
}

interface CounterViewBinding : ViewBinding {

    val incAction: Action<Unit>
    val decAction: Action<Unit>
    val counterBond: Bond<Int>
}

/**
 * Презентер для навигации
 */
@PerScreen
class MainNavigationPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                  private val activityNavigator: ActivityNavigator)
    : BaseRxPresenter<MainNavigationViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var vb: MainNavigationViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(vb) {
            checkboxSampleActivityOpen bindTo { activityNavigator.start(CheckboxActivityRoute()) }
            easyadapterSampleActivityOpen bindTo { activityNavigator.start(EAMainActivityRoute()) }
        }
    }
}

interface MainNavigationViewBinding : ViewBinding {

    val checkboxSampleActivityOpen: Action<Unit>
    val easyadapterSampleActivityOpen: Action<Unit>
}

/**
 * Презентер управления [SampleDialog]
 */
@PerScreen
class DialogControlPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val dialogNavigator: DialogNavigator)
    : BaseRxPresenter<DialogControlViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var vb: DialogControlViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(vb) {
            dialogOpenAction bindTo { dialogNavigator.show(SampleDialogRoute()) }
            dialogPositiveAction bindTo { messageCommand.accept("Dialog close by OK") }
            dialogNegativeAction bindTo { messageCommand.accept("Dialog close by not OK") }
        }
    }
}

interface DialogControlViewBinding : SampleDialogViewBinding {

    val dialogOpenAction: Action<Unit>
    val messageCommand: Command<String>
}