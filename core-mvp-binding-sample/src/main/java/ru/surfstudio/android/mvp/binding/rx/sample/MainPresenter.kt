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
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter<MainViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var bm: MainViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
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
    : ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter<DoubleTextViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var bm: DoubleTextViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            doubleTextAction bindTo { textEditBond.change { it + it } }
        }
    }
}

interface DoubleTextViewBinding : ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel {

    val doubleTextAction: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>
    val textEditBond: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond<String>
}

/**
 * Презентер для инеркмента и декремента значений
 */
@PerScreen
class CounterPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter<CounterViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var bm: CounterViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            incAction bindTo { counterBond.change { it.inc() } }
            decAction bindTo { counterBond.change { it.dec() } }
        }
    }
}

interface CounterViewBinding : ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel {

    val incAction: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>
    val decAction: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>
    val counterBond: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond<Int>
}

/**
 * Презентер для навигации
 */
@PerScreen
class MainNavigationPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                  private val activityNavigator: ActivityNavigator)
    : ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter<MainNavigationViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var bm: MainNavigationViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            checkboxSampleActivityOpen bindTo { activityNavigator.start(CheckboxActivityRoute()) }
            easyadapterSampleActivityOpen bindTo { activityNavigator.start(EAMainActivityRoute()) }
        }
    }
}

interface MainNavigationViewBinding : ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel {

    val checkboxSampleActivityOpen: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>
    val easyadapterSampleActivityOpen: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>
}

/**
 * Презентер управления [SampleDialog]
 */
@PerScreen
class DialogControlPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val dialogNavigator: DialogNavigator)
    : ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter<DialogControlViewBinding>(basePresenterDependency) {

    @Inject
    override lateinit var bm: DialogControlViewBinding

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(bm) {
            dialogOpenAction bindTo { dialogNavigator.show(SampleDialogRoute()) }
            dialogPositiveAction bindTo { messageCommand.accept("Dialog close by OK") }
            dialogNegativeAction bindTo { messageCommand.accept("Dialog close by not OK") }
        }
    }
}

interface DialogControlViewBinding : SampleDialogViewBinding {

    val dialogOpenAction: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Action<Unit>
    val messageCommand: ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command<String>
}