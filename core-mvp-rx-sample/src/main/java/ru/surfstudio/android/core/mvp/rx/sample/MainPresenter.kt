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

        subscribe(model.incAction.getObservable()) { model.counterState.getConsumer().accept(100) }
        subscribe(model.decAction.getObservable()) { model.counterState.getConsumer().accept(20) }
        subscribe(model.textEditState.getObservable()){ model.sampleCommand.getConsumer().accept(it)}
        subscribe(model.doubleTextAction.getObservable()) { model.textEditState.apply { getConsumer().accept(value + value) } }

        subscribe(model.checkboxSampleActivityOpen.getObservable()) { activityNavigator.start(CheckboxActivityRoute()) }
        subscribe(model.cycledSampleActivityOpen.getObservable()) { activityNavigator.start(CycledActivityRoute()) }
    }
}