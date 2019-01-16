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
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        val activityNavigator: ActivityNavigator
) : BaseRxPresenter<MainModel, MainActivityView>(basePresenterDependency) {

    private val model = MainModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        model.incAction.bindTo(model.counterState) { 100 }
        model.decAction.bindTo(model.counterState) { 20 }
        model.textEditState bindTo model.sampleCommand
        model.doubleTextAction.bindTo(model.textEditState) { model.textEditState.let { it.value + it.value } }

        model.checkboxSampleActivityOpen bindTo { activityNavigator.start(CheckboxActivityRoute()) }
        model.cycledSampleActivityOpen bindTo { activityNavigator.start(CycledActivityRoute()) }
    }
}