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

import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Bond

class BondTest : BaseRelationTest() {

    private lateinit var bond: Bond<String>

    private lateinit var testViewObservable: TestObserver<String>
    private lateinit var testViewConsumer: Consumer<String>

    private lateinit var testPresenterObservable: TestObserver<String>
    private lateinit var testPresenterConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        bond = Bond()

        testViewConsumer =
                with(testView) {
                    bond.consumer
                }

        testViewObservable =
                with(testView) {
                    bond.observable.test()
                }

        testPresenterConsumer =
                with(testPresenter) {
                    bond.consumer
                }

        testPresenterObservable =
                with(testPresenter) {
                    bond.observable.test()
                }
    }

    @Test
    @Throws(Exception::class)
    fun test() {
        assertNotEquals(testViewObservable, testPresenterObservable)
        assertNotEquals(testViewConsumer, testPresenterConsumer)

        testViewObservable
                .assertNoValues()
                .assertNoErrors()

        testPresenterObservable
                .assertNoValues()
                .assertNoErrors()

        assertFalse(bond.hasValue)

        testPresenterConsumer.accept("TEST_FROM_PRESENTER")
        testViewObservable
                .assertValueCount(1)

        assertTrue(bond.hasValue)

        with(testView) {
            assertEquals("TEST_FROM_PRESENTER", bond.value)
        }

        testViewConsumer.accept("TEST_FROM_VIEW")
        testPresenterObservable
                .assertValueCount(1)

        assertTrue(bond.hasValue)

        with(testPresenter) {
            assertEquals("TEST_FROM_VIEW", bond.value)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testInitialValue() {
        val bond = Bond("Initial")
        with(testView) {
            var newValue = ""
            bond bindTo { newValue = it }
            assertEquals("Initial", newValue)
        }
        with(testPresenter) {
            var newValue = ""
            bond bindTo { newValue = it }
            assertEquals("Initial", newValue)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testDiffValue() {
        val bond = Bond<String>()
        with(testView) {
            bond.accept("view_value")
        }
        with(testPresenter) {
            bond.accept("presenter_value")
        }
        with(testView) {
            var newValue = ""
            bond bindTo { newValue = it }
            assertEquals("presenter_value", newValue)
            assertEquals("presenter_value", bond.value)
        }
        with(testPresenter) {
            var newValue = ""
            bond bindTo { newValue = it }
            assertEquals("view_value", newValue)
            assertEquals("view_value", bond.value)
        }
    }
}