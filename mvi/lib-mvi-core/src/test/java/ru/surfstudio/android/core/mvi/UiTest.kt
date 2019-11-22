package ru.surfstudio.android.core.mvi

import androidx.annotation.CallSuper
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UiTest : BaseReactTest() {

    lateinit var testViewStateObservable: TestObserver<String>
    lateinit var testReactor: TestReactor
    lateinit var testMiddleware: UiMiddleware


    @Before
    @Throws(Exception::class)
    @CallSuper
    override fun setUp() {
        super.setUp()
        testViewStateObservable = testView.run { holder.state.observable.test() }
        testMiddleware = UiMiddleware()
        testReactor = TestReactor()
        testBinder = TestBinder().apply { bind(testView.hub, testMiddleware, testView.holder, testReactor) }
    }


    @Test
    fun testEventsPassingReactor() {
        assertEquals(0, testReactor.eventsCount)

        testView.hub.emit(TestEvent.Logic())
        Thread.sleep(100)
        assertEquals(1, testReactor.eventsCount)

        testView.hub.emit(TestEvent.Ui())
        Thread.sleep(100)
        assertEquals(3, testReactor.eventsCount) //reactor gets both Ui and Success event
    }

    @Test
    fun testStateChanges() {
        testViewStateObservable.assertNoValues()

        testView.hub.emit(TestEvent.Ui())
        Thread.sleep(100)
        testViewStateObservable
                .assertValueCount(1)
                .assertValue("data")

        testView.hub.emit(TestEvent.Data("newData"))
        Thread.sleep(100)
        testViewStateObservable
                .assertValueCount(2)
                .assertValueAt(1, "newData")
    }

    @Test
    fun testStateUnchangedOnLogic() {
        testViewStateObservable.assertNoValues()

        testView.hub.emit(TestEvent.Logic())
        testViewStateObservable.assertValueCount(0)
    }
}