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
                    state.getObservable().test()
                }

        testConsumer =
                with(testPresenter) {
                    state.getConsumer()
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
}