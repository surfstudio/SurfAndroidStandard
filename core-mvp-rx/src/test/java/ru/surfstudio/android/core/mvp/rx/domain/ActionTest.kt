package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ActionTest: BaseRelationTest() {

    private lateinit var action: Action<String>

    private lateinit var testObservable: TestObserver<String>
    private lateinit var testConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        action = Action()

        testObservable =
                with(testPresenter) {
                    action.getObservable().test()
                }


        testConsumer =
                with(testView) {
                    action.getConsumer()
                }
    }

    @Test
    @Throws(Exception::class)
    fun test() {
        testObservable
                .assertNoValues()
                .assertNoErrors()

        assertFalse(action.hasValue)

        testConsumer.accept("TEST")

        testObservable
                .assertValueCount(1)

        assertTrue(action.hasValue)
        assertEquals("TEST", action.value)
    }
}