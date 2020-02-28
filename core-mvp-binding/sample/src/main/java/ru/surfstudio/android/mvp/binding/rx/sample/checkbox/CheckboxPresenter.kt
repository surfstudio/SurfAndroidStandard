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

package ru.surfstudio.android.mvp.binding.rx.sample.checkbox

import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана [CheckboxActivityView]
 */
@PerScreen
class CheckboxPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: CheckboxBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()

        // комбинирование экшенов
        val checkboxesObs =
                Observables.combineLatest(
                        bm.checkBond1.observable,
                        bm.checkAction2.observable,
                        bm.checkAction3.observable
                ) { b1, b2, b3 -> Triple(b1, b2, b3) }

        checkboxesObs.map { it.first.toInt() + it.second.toInt() + it.third.toInt() } bindTo bm.count

        // показывает сообщени по нажатию кнопки с последним состоянием checkboxesObs
        bm.sendAction.observable
                .withLatestFrom(checkboxesObs)
                { _, triple -> "cb1: ${triple.first}, cb2: ${triple.second}, cb3: ${triple.third}" }
                .bindTo(bm.messageCommand)

        with(bm) {
            checkBond1.accept(route.isCheckFirst)
        }
    }
}

private fun Boolean.toInt() = if (this) 1 else 0