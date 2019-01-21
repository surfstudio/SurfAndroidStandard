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

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.data.MainScreenModel
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.data.ScreenModelFactory
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class EAMainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BaseRxPresenter<MainScreenModel, EAMainActivityView>(basePresenterDependency) {

    override fun getRxModel(): MainScreenModel = MainScreenModel()

    private val INTERVAL: Long = 2500 //ms
    private val screenModelFactory = ScreenModelFactory()
    private var screenModel: MainScreenModel = screenModelFactory.next()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
        if (!viewRecreated) {
            subscribe(Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread()),
                    { _, _ -> true },
                    { _ -> view.render(screenModelFactory.next()) },
                    { _ -> /*ignore*/ })
        }
    }
}
