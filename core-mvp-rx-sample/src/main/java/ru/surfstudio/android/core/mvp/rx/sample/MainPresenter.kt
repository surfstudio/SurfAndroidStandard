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
import ru.surfstudio.android.core.mvp.rx.sample.cycled.CycledActivityRoute
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.EAMainActivityRoute
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BaseRxPresenter<MainModel, MainActivityView>(basePresenterDependency) {

    override val pm = MainModel()

    override fun onFirstLoad() {
        super.onFirstLoad()

        pm.incAction.observable.map { 100 } bindTo pm.counterState
        pm.decAction.observable.map { 20 } bindTo pm.counterState
        pm.doubleTextAction.observable.map { pm.textEditState.let { it.value + it.value } } bindTo pm.textEditState
        pm.textEditState bindTo pm.sampleCommand

        pm.checkboxSampleActivityOpen bindTo { activityNavigator.start(CheckboxActivityRoute()) }
        pm.cycledSampleActivityOpen bindTo { activityNavigator.start(CycledActivityRoute()) }
        pm.easyadapterSampleActivityOpen bindTo { activityNavigator.start(EAMainActivityRoute()) }
    }
}