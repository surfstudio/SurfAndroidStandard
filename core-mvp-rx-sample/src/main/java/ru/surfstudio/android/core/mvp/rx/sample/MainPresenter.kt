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
import ru.surfstudio.android.core.mvp.rx.sample.checkbox.CheckboxActivityRoute
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.EAMainActivityRoute
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import javax.inject.Inject

/**
 * Презентер главного экрана примеров
 */
@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val dialogNavigator: DialogNavigator
) : BaseRxPresenter<MainViewBinding>(basePresenterDependency) {

    override val vb = MainViewBinding()

    override fun onFirstLoad() {
        super.onFirstLoad()
        with(vb) {
            incAction bindTo { counterBond.change { it.inc() } }
            decAction bindTo { counterBond.change { it.dec() } }
            doubleTextAction bindTo { textEditBond.change { it + it } }

            dialogPositiveAction bindTo { messageCommand.accept("Dialog close by OK") }
            dialogNegativeAction bindTo { messageCommand.accept("Dialog close by not OK") }

            textEditBond bindTo sampleState
            dialogInputAction bindTo sampleState

            dialogOpenAction bindTo { dialogNavigator.show(SampleDialogRoute()) }
            checkboxSampleActivityOpen bindTo { activityNavigator.start(CheckboxActivityRoute()) }
            easyadapterSampleActivityOpen bindTo { activityNavigator.start(EAMainActivityRoute()) }
        }
    }
}