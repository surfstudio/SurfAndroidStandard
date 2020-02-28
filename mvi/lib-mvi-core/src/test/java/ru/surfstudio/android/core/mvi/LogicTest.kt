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
