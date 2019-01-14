package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StateTest : BaseRelationTest() {

    private lateinit var state: State<String>

    private lateinit var testViewObservable: TestObserver<String>
    private lateinit var testViewConsumer: Consumer<String>

    private lateinit var testPresenterObservable: TestObserver<String>
    private lateinit var testPresenterConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        state = State()

        testViewConsumer =
                with(testView) {
                    state.getConsumer()
                }

        testViewObservable =
                with(testView) {
                    state.getObservable().test()
                }

        testPresenterConsumer=
                with(testPresenter) {
                    state.getConsumer()
                }

        testPresenterObservable =
                with(testPresenter) {
                    state.getObservable().test()
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

        assertFalse(state.hasValue)

        testPresenterConsumer.accept("TEST_FROM_PRESENTER")
        testViewObservable
                .assertValueCount(1)

        assertTrue(state.hasValue)

        assertEquals("TEST_FROM_PRESENTER", state.value)

        testViewConsumer.accept("TEST_FROM_VIEW")
        testPresenterObservable
                .assertValueCount(1)

        assertTrue(state.hasValue)

        assertEquals("TEST_FROM_VIEW", state.value)
    }
}