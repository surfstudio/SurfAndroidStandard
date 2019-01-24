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

package ru.surfstudio.android.core.mvp.rx.relation

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.junit.Before
import ru.surfstudio.android.core.mvp.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.rx.relation.mvp.VIEW

abstract class BaseRelationTest {

    lateinit var testView: Related<VIEW>
    lateinit var testPresenter: Related<PRESENTER>

    @Before
    @Throws(Exception::class)
    @CallSuper
    open fun setUp() {
        testView = TestView()

        testPresenter = TestPresenter()
    }
}

class TestView : Related<VIEW> {
    override fun relationEntity(): VIEW = VIEW

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>): Disposable =
            observable.subscribe(onNext)
}

class TestPresenter : Related<PRESENTER> {
    override fun relationEntity(): PRESENTER = PRESENTER

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>): Disposable =
            observable.subscribe(onNext)
}