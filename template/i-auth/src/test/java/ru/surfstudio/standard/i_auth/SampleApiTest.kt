package ru.surfstudio.standard.i_auth

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.surfstudio.standard.test_utils.ApiTestRunner
import ru.surfstudio.standard.test_utils.BaseNetworkDaggerTest
import ru.surfstudio.standard.test_utils.app.TestApp
import ru.surfstudio.standard.test_utils.WaitApiTest

@RunWith(ApiTestRunner::class)
@Config(application = TestApp::class, sdk = [Build.VERSION_CODES.O_MR1])
class SampleApiTest : BaseNetworkDaggerTest<AuthApiTestComponent>() {

    override val component: AuthApiTestComponent = DaggerAuthApiTestComponent.builder()
            .testNetworkAppComponent(networkAppComponent)
            .build()

    override fun inject(component: AuthApiTestComponent) {
        component.inject(this)
    }

    @Test
    fun simpleApiTest() {
        System.out.println("simpleApiTest")

        val i = 0
    }

    @Test
    fun firstApiTest() {
        System.out.println("firstWaitApiTest")
        throw RuntimeException("Test fail 11111")
    }

    @Test
    @WaitApiTest
    fun secondWaitApiTest() {
        System.out.println("secondWaitApiTest")
        //throw RuntimeException("Test fail 11111")
    }

    @Test
    @WaitApiTest(RuntimeException::class)
    fun thirdWaitApiTest() {
        System.out.println("thirdWaitApiTest")
        throw RuntimeException("Checked Exception. Test success")
    }
}