/*
  Copyright (c) 2020, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package ru.surfstudio.android.core.mvi

import androidx.annotation.CallSuper
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Before
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.ui.binder.RxBinder
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW

abstract class BaseReactTest {

    lateinit var testView: TestView
    lateinit var testBinder: TestBinder

    @Before
    @Throws(Exception::class)
    @CallSuper
    open fun setUp() {
        testView = TestView()
    }

    @After
    fun destroy() {
        testView.onDestroy()
        testBinder.onDestroy()
    }
}

sealed class TestEvent : Event {
    class Logic : TestEvent()
    class Ui : TestEvent()
    class Data(val value: String) : TestEvent()
}

class TestHub : RxEventHub<TestEvent> {

    val relay = PublishRelay.create<TestEvent>()

    override fun emit(event: TestEvent) {
        accept(event)
    }

    override fun observe(): Observable<TestEvent> =
            relay.hide()

    override fun accept(t: TestEvent) {
        relay.accept(t)
    }
}

class TestView : Related<VIEW> {

    private val disposables = CompositeDisposable()

    val holder = TestStateHolder()
    val hub = TestHub()

    fun onDestroy() {
        disposables.clear()
    }

    override fun relationEntity(): VIEW = VIEW

    override fun <T> subscribe(observable: Observable<out T>,
                               onNext: Consumer<T>,
                               onError: (Throwable) -> Unit): Disposable =
            observable.subscribe(onNext)
                    .apply { disposables.add(this) }

}

class TestStateHolder {
    val state = State<String>()
}


class TestReactor : Reactor<TestEvent, TestStateHolder> {

    var eventsCount = 0

    override fun react(sh: TestStateHolder, event: TestEvent) {
        eventsCount++
        when (event) {
            is TestEvent.Data -> sh.state.accept(event.value)
            else -> {
                //do nothing
            }
        }
    }
}

abstract class TestMiddleware : RxMiddleware<TestEvent> {

    override fun transform(eventStream: Observable<TestEvent>): Observable<out TestEvent> =
            eventStream.flatMap(::flatMap)

    abstract fun flatMap(event: TestEvent): Observable<out TestEvent>
}

class LogicMiddleware : TestMiddleware() {

    var eventsCount = 0

    override fun flatMap(event: TestEvent): Observable<out TestEvent> = doAndSkip { eventsCount++ }
}

class UiMiddleware : TestMiddleware() {

    override fun flatMap(event: TestEvent): Observable<out TestEvent> = when (event) {
        is TestEvent.Ui -> loadData()
        else -> skip()
    }

    fun loadData() = Observable.just(TestEvent.Data("data"))
}

class TestBinder : RxBinder {

    private val disposables = CompositeDisposable()

    fun onDestroy() {
        disposables.clear()
    }

    override fun <T> subscribe(
            observable: Observable<T>,
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit
    ) = observable.subscribe(onNext, onError).also { disposables.add(it) }
}