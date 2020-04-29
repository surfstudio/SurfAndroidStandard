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

import org.junit.Assert
import org.junit.Test

class LogicTest : BaseReactTest() {

    lateinit var testMiddleware: LogicMiddleware

    override fun setUp() {
        super.setUp()
        testMiddleware = LogicMiddleware()
        testBinder = TestBinder().apply { bind(testView.hub, testMiddleware) }
    }

    @Test
    fun testEventsPassingMiddleware() {
        Assert.assertEquals(0, testMiddleware.eventsCount)

        testView.hub.emit(TestEvent.Ui())
        testView.hub.emit(TestEvent.Logic())
        testView.hub.emit(TestEvent.Data(""))

        Assert.assertEquals(3, testMiddleware.eventsCount)
    }
}
