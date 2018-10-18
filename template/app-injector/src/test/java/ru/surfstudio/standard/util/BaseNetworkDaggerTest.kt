package ru.surfstudio.standard.util

import android.os.Build
import android.security.NetworkSecurityPolicy
import android.support.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.logger.logging_strategies.impl.test.TestLoggingStrategy
import ru.surfstudio.standard.app_injector.App
import ru.surfstudio.standard.app_injector.AppModule

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class,
        sdk = [Build.VERSION_CODES.O_MR1])
abstract class BaseNetworkDaggerTest {

    companion object {
        private val testLoggingStrategy = TestLoggingStrategy()
    }

    @Suppress("DEPRECATION")
    private val networkComponent = DaggerTestNetworkAppComponent.builder()
            .appModule(AppModule(RuntimeEnvironment.application, ActiveActivityHolder()))
            .build()

    abstract fun inject(networkComponent: TestNetworkAppComponent)

    @Before
    @CallSuper
    open fun setUp() {
        Logger.addLoggingStrategy(testLoggingStrategy)
        inject(networkComponent)
    }

    @After
    @CallSuper
    open fun tearDown() {
        Logger.removeLoggingStrategy(testLoggingStrategy)
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
     * shadows = [BaseNetworkDaggerTest.TestNetworkSecurityPolicy::class] (при необходимости)
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
                error(e)
            }
        }
    }
}
