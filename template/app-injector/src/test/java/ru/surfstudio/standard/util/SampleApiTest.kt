package ru.surfstudio.standard.util

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.surfstudio.standard.app_injector.App

@RunWith(ApiTestRunner::class)
@Config(application = App::class,
        sdk = [Build.VERSION_CODES.O_MR1])
class SampleApiTest : BaseNetworkDaggerTest() {

    override fun inject(networkComponent: TestNetworkAppComponent) {
        networkComponent.inject(this)
    }

    @Test
    @CheckApiTest
    fun testOne() {
        System.out.println("one")

        val i = 0
    }

    @Test
    @WaitApiTest
    fun waitTestOne() {
        System.out.println("wait one")
        throw RuntimeException("Test fail 11111")
    }

    @Test
    @WaitApiTest
    fun waitTes3() {
        System.out.println("3")
        //throw RuntimeException("Test fail 11111")
    }
}