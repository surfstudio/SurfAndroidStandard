package ru.surfstudio.standard.util

import android.support.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.standard.app_injector.App
import ru.surfstudio.standard.app_injector.AppModule
import ru.surfstudio.standard.util.modules.TestOkHttpModule

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class, manifest = Config.NONE)
abstract class BaseDaggerTest {

    private val networkComponent = DaggerTestNetworkAppComponent.builder()
            .appModule(AppModule(RuntimeEnvironment.application as CoreApp))
            .testOkHttpModule(TestOkHttpModule())
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
}
