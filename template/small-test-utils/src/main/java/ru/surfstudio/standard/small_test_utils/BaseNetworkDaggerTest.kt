package ru.surfstudio.standard.small_test_utils

import android.security.NetworkSecurityPolicy
import androidx.annotation.CallSuper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.logger.logging_strategies.impl.test.TestLoggingStrategy
import ru.surfstudio.standard.small_test_utils.di.components.DaggerTestNetworkAppComponent
import ru.surfstudio.standard.small_test_utils.di.components.TestNetworkAppComponent
import ru.surfstudio.standard.small_test_utils.di.modules.TestAppModule

abstract class BaseNetworkDaggerTest<T> {

    companion object {
        private val testLoggingStrategy = TestLoggingStrategy()
    }

    @Suppress("DEPRECATION")
    val networkAppComponent: TestNetworkAppComponent = DaggerTestNetworkAppComponent.builder()
            .testAppModule(TestAppModule(RuntimeEnvironment.application, ActiveActivityHolder()))
            .build()

    abstract val component: T

    abstract fun inject()

    @Before
    @CallSuper
    open fun setUp() {
        Logger.addLoggingStrategy(testLoggingStrategy)
        inject()
    }

    @After
    @CallSuper
    open fun tearDown() {
        Logger.removeLoggingStrategy(testLoggingStrategy)
    }

    protected fun <T> test(observable: Observable<T>): TestObserver<T> {
        return observable.test().assertNoErrors()
    }

    protected fun <T> test(single: Single<T>): TestObserver<T> {
        return single.test().assertNoErrors()
    }

    protected fun test(completable: Completable): TestObserver<T> {
        return test(completable.toObservable())
    }

    protected fun <T> testAndGetValues(observable: Observable<T>): List<T> {
        return test(observable).values()
    }

    protected fun <T> testAndGetFirstValue(observable: Observable<T>): T {
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
