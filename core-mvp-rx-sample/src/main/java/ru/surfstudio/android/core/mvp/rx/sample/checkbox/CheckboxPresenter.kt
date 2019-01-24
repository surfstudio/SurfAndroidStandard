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

import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import javax.inject.Inject

class CheckboxPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter<CheckboxModel, CheckboxActivityView>(basePresenterDependency) {

    override val pm = CheckboxModel()

    override fun onFirstLoad() {
        super.onFirstLoad()

        val checkboxesObs =
                Observables.combineLatest(
                        pm.checkAction1.observable,
                        pm.checkAction2.observable,
                        pm.checkAction3.observable
                ) { b1, b2, b3 -> Triple(b1, b2, b3) }

        checkboxesObs.map { it.first.toInt() + it.second.toInt() + it.third.toInt() } bindTo pm.count

        pm.sendAction.observable
                .withLatestFrom(checkboxesObs)
                { _, triple -> "cb1: ${triple.first}, cb2: ${triple.second}, cb3: ${triple.third}" }
                .bindTo(pm.messageCommand)
    }
}

private fun Boolean.toInt() = if (this) 1 else 0