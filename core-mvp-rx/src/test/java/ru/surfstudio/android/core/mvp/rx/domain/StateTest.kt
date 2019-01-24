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

package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
        assertEquals("TEST", state.value)
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
}