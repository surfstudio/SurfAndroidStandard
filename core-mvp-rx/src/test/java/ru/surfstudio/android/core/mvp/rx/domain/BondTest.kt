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

class BondTest : BaseRelationTest() {

    private lateinit var twoWay: Bond<String>

    private lateinit var testViewObservable: TestObserver<String>
    private lateinit var testViewConsumer: Consumer<String>

    private lateinit var testPresenterObservable: TestObserver<String>
    private lateinit var testPresenterConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        twoWay = Bond()

        testViewConsumer =
                with(testView) {
                    twoWay.getConsumer()
                }

        testViewObservable =
                with(testView) {
                    twoWay.getObservable().test()
                }

        testPresenterConsumer=
                with(testPresenter) {
                    twoWay.getConsumer()
                }

        testPresenterObservable =
                with(testPresenter) {
                    twoWay.getObservable().test()
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

        assertFalse(twoWay.hasValue)

        testPresenterConsumer.accept("TEST_FROM_PRESENTER")
        testViewObservable
                .assertValueCount(1)

        assertTrue(twoWay.hasValue)

        assertEquals("TEST_FROM_PRESENTER", twoWay.value)

        testViewConsumer.accept("TEST_FROM_VIEW")
        testPresenterObservable
                .assertValueCount(1)

        assertTrue(twoWay.hasValue)

        assertEquals("TEST_FROM_VIEW", twoWay.value)
    }

    @Test
    @Throws(Exception::class)
    fun testCycled() {
        val bound1 = Bond<String>()
        val bound2 = Bond<String>()

        with(testView) {
            bound1 bindTo bound2
            bound2 bindTo bound1
        }

        with(testPresenter) {
            bound1 bindTo bound2
            bound2 bindTo bound1
        }

        bound1.getConsumer(VIEW).accept("1")

        assertEquals("1", bound1.value)
        assertEquals("1", bound2.value)
    }
}