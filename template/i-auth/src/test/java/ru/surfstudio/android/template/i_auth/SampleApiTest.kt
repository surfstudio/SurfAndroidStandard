package ru.surfstudio.android.template.i_auth

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.surfstudio.android.template.test_utils.ApiTestRunner
import ru.surfstudio.android.template.test_utils.BaseNetworkDaggerTest
import ru.surfstudio.android.template.test_utils.WaitApiTest
import ru.surfstudio.android.template.test_utils.app.TestApp

@RunWith(ApiTestRunner::class)
@Config(application = TestApp::class,
        sdk = [Build.VERSION_CODES.O_MR1])
class SampleApiTest : BaseNetworkDaggerTest<AuthApiTestComponent>() {

    override val component: AuthApiTestComponent = DaggerAuthApiTestComponent.builder()
            .testNetworkAppComponent(networkAppComponent)
            .build()

    override fun inject(component: AuthApiTestComponent) {
        component.inject(this)
    }

    @Test
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
    fun waitTest3() {
        System.out.println("3")
        //throw RuntimeException("Test fail 11111")
    }

    @Test
    @WaitApiTest(RuntimeException::class)
    fun waitTest4() {
        System.out.println("4")
        throw RuntimeException("Checked Exception. Test success")
    }
}