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

import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindModel

class StateTest : BaseRelationTest() {

    private lateinit var state: State<String>

    private lateinit var testObservable: TestObserver<String>
    private lateinit var testConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        state = State()

        testObservable =
            with(testView) {
                state.observable.test()
            }

        testConsumer =
            with(testPresenter) {
                state.consumer
            }
    }

    @Test
    @Throws(Exception::class)
    fun test() {
        testObservable
            .assertNoValues()
            .assertNoErrors()

        assertFalse(state.hasValue)

        testConsumer.accept("TEST")

        testObservable
            .assertValueCount(1)

        assertTrue(state.hasValue)
        with(testPresenter) {
            assertEquals("TEST", state.value)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testInitialValue() {
        with(testView) {
            val state = State("Initial")
            var newValue = ""
            state bindTo { newValue = it }
            assertEquals("Initial", newValue)
        }
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        with(testView) {
            val state = State("Update")
            var newValue = ""
            state bindTo { newValue += it }
            state.update()
            assertEquals("UpdateUpdate", newValue)

            disposables.dispose()
            state.observable.test().dispose()
            assert(state.observable.test().isDisposed.not())
        }
    }

    @Test
    @Throws(Exception::class)
    fun variance() {
        open class A
        class B : A()

        val myInteractor = object {
            val foo = PublishSubject.create<B>()
        }
        val myBindModel = object : BindModel {
            val state = State<A>()
        }

        with(testPresenter) {
            myInteractor.foo bindTo myBindModel.state

            myInteractor.foo.onNext(B())

            val state = myBindModel.state
            var newValue = A()
            state bindTo { newValue = it }
            assertTrue(newValue is B)
        }
    }
}
