package ru.surfstudio.standard.i_auth

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.surfstudio.standard.i_auth.di.DaggerTestAuthApiComponent
import ru.surfstudio.standard.i_auth.di.TestAuthApiComponent
import ru.surfstudio.standard.small_test_utils.ApiTestRunner
import ru.surfstudio.standard.small_test_utils.BaseNetworkDaggerTest
import ru.surfstudio.standard.small_test_utils.app.TestApp
import ru.surfstudio.standard.small_test_utils.WaitApiTest
import javax.inject.Inject

@RunWith(ApiTestRunner::class)
@Config(application = TestApp::class, sdk = [Build.VERSION_CODES.O_MR1])
class SampleApiTest : BaseNetworkDaggerTest<TestAuthApiComponent>() {

    @Inject
    lateinit var authRepository: AuthRepository

    override val component: TestAuthApiComponent = DaggerTestAuthApiComponent.builder()
            .testNetworkAppComponent(networkAppComponent)
            .build()

    override fun inject() {
        component.inject(this)
    }

    @Test
    fun simpleApiTest() {

    }

    @Test
    @WaitApiTest(RuntimeException::class)
    fun simpleWaitApiTest() {
        throw RuntimeException("Checked Exception. Test success")
    }
}