package ru.surfstudio.standard.util

import android.security.NetworkSecurityPolicy
import android.support.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.standard.app_injector.App
import ru.surfstudio.standard.app_injector.AppModule

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class)
abstract class BaseDaggerTest {

    @Suppress("DEPRECATION")
    private val networkComponent = DaggerTestNetworkAppComponent.builder()
            .appModule(AppModule(RuntimeEnvironment.application as CoreApp))
            .build()

    abstract fun inject(networkComponent: TestNetworkAppComponent)

    @Before
    @CallSuper
    open fun setUp() {
        inject(networkComponent)
    }

    protected fun<T> test(observable: Observable<T>): TestObserver<T> {
        return observable.test().assertNoErrors()
    }

    protected fun<T> testAndGetValues(observable: Observable<T>): List<T> {
        return test(observable).values()
    }

    protected fun<T> testAndGetFirstValue(observable: Observable<T>): T {
        return testAndGetValues(observable).first()
    }

    /**
     * Добавить в @Config
     * shadows = [BaseDaggerTest.TestNetworkSecurityPolicy::class] (при необходимости)
     */
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
