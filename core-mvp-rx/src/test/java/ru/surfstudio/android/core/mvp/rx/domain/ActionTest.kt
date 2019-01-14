package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import ru.surfstudio.android.core.mvp.rx.ui.BindableRxView
import org.mockito.junit.MockitoJUnit.rule
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

class ActionTest: BaseRelationTest() {

    lateinit var action: Action<String>

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

        testConsumer.accept("TEST")

        testObservable
                .assertValueCount(1)

        assertEquals("TEST", action.value)
    }
}