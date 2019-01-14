package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CommandTest : BaseRelationTest() {

    private lateinit var command: Command<String>

    private lateinit var testObservable: TestObserver<String>
    private lateinit var testConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        command = Command()

        testObservable =
                with(testView) {
                    command.getObservable().test()
                }

        testConsumer =
                with(testPresenter) {
                    command.getConsumer()
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

        assertEquals("TEST", command.value)
    }
}