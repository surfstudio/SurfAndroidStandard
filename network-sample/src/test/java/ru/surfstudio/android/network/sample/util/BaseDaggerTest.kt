package ru.surfstudio.android.network.sample.util

import android.support.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.surfstudio.android.network.sample.app.CustomApp
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import android.security.NetworkSecurityPolicy
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@RunWith(RobolectricTestRunner::class)
@Config(
        application = CustomApp::class,
        manifest = Config.NONE,
        shadows = [BaseDaggerTest.TestNetworkSecurityPolicy::class])
abstract class BaseDaggerTest {

    @Suppress("DEPRECATION")
    private val networkComponent = DaggerTestNetworkAppComponent.builder()
            .defaultAppModule(DefaultAppModule(RuntimeEnvironment.application as CustomApp))
            .build()

    abstract fun inject(networkComponent: TestNetworkAppComponent)

    @Before
    @CallSuper
    open fun setUp() {
        inject(networkComponent)
    }

    protected fun <T> test(observable: Observable<T>): TestObserver<T> {
        return observable.test().assertNoErrors()
    }

    protected fun <T> testAndGetValues(observable: Observable<T>): List<T> {
        return test(observable).values()
    }

    protected fun <T> testAndGetFirstValue(observable: Observable<T>): T {
        return testAndGetValues(observable).first()
    }

    @Implements(NetworkSecurityPolicy::class)
    class TestNetworkSecurityPolicy {

        @Implementation
        fun isCleartextTrafficPermitted(host: String): Boolean = true

        companion object {

            private const val NETWORK_SECURITY_POLICY_CLASS_NAME = "android.security.NetworkSecurityPolicy"

            @Implementation
            @JvmStatic
            fun getInstance(): NetworkSecurityPolicy = try {
                Class.forName(NETWORK_SECURITY_POLICY_CLASS_NAME).newInstance() as NetworkSecurityPolicy
            } catch (e: Exception) {
                throw AssertionError()
            }
        }
    }
}
