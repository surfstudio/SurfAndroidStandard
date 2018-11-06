import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory.only
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.BaseScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.state.ActivityScreenState
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * unit tests for [BasePresenter]
 */

@RunWith(PowerMockRunner::class)
@PrepareForTest(Logger::class)
class MockPresenterTest {

    @Mock
    var connectionProvider: ConnectionProvider? = null
    @Mock
    lateinit var basePresenterDependency: BasePresenterDependency
    @Mock
    private val onSuccess: (String) -> Unit = {}
    @Mock
    private val onSuccessMayBe: (Any) -> Unit = { }
    @Mock
    private val onError: (Throwable) -> Unit = {}
    @Mock
    private val onComplete: () -> Unit = {}
    @Mock
    private val reloadAction: () -> Unit = {}

    internal lateinit var presenter: MockPresenter

    private val single = Single.just("Single")
    private val maybe = Maybe.just("Maybe")
    private val maybeAction = Maybe.fromAction<Any> { val i = 1 }
    private val completable = Completable.complete()

    private val testThrowable = Throwable("test")
    private val singleError = Single.error<String>(testThrowable)
    private val maybeError = Maybe.error<String>(testThrowable)
    private val completableError = Completable.error(testThrowable)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(Logger::class.java)

        val schedulersProvider = object : SchedulersProvider {
            override fun main(): Scheduler {
                return Schedulers.trampoline() //аналог immediate() в RxJava 2
            }

            override fun worker(): Scheduler {
                return Schedulers.trampoline() //аналог immediate() в RxJava 2
            }
        }

        basePresenterDependency = BasePresenterDependency(schedulersProvider,
                mock(ActivityScreenState::class.java),
                mock(BaseScreenEventDelegateManager::class.java),
                mock(ErrorHandler::class.java),
                connectionProvider,
                mock(ActivityNavigator::class.java))
        presenter = MockPresenter(basePresenterDependency)
    }

    //region subscribeIoTests
    @Test
    fun subscribeSingle() {
        presenter.testSubscribeIo(single, onSuccess, onError)
        verify(onSuccess, only()).invoke("Single")
    }

    @Test
    fun subscribeCompletable() {
        presenter.testSubscribeIo(completable, onComplete, onError)
        verify(onComplete, only()).invoke()
    }

    @Test
    fun subscribeMaybe() {
        presenter.testSubscribeIo(maybe, onSuccess, onComplete, onError)
        verify<(String) -> Unit>(onSuccess, only()).invoke("Maybe")
    }

    @Test
    fun subscribeMaybeAction() {
        val onSuccess: (Any) -> Unit = { }

        presenter.testSubscribeIo(maybeAction, onSuccess, onComplete, onError)
        verify(onComplete, only()).invoke()
    }

    @Test
    fun subscribeSingleError() {
        presenter.testSubscribeIo(singleError, onSuccess, onError)
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }

    @Test
    fun subscribeCompletableError() {
        presenter.testSubscribeIo(completableError, onComplete, onError)
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }

    @Test
    fun subscribeMaybeError() {
        val onSuccess: (Any) -> Unit = { }

        presenter.testSubscribeIo(maybeError, onSuccess, onComplete, onError)
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }
    //endregion

    //region subscribeIoHandlerError
    @Test
    fun subscribeIoHandleErrorSingle() {
        presenter.testSubscribeIoHandleError(single, onSuccess, onError)
        verify<(String) -> Unit>(onSuccess, only()).invoke("Single")
    }

    @Test
    fun subscribeIoHandleErrorCompletable() {
        presenter.testSubscribeIoHandleError(completable, onComplete, onError)
        verify(onComplete, only()).invoke()
    }

    @Test
    fun subscribeIoHandleErrorMaybe() {
        presenter.testSubscribeIoHandleError(maybe, onSuccessMayBe, onComplete, onError)
        verify<(String) -> Unit>(onSuccessMayBe, only()).invoke("Maybe")
    }

    @Test
    fun subscribeIoHandleErrorMaybeAction() {
        presenter.testSubscribeIoHandleError(maybeAction, onSuccessMayBe, onComplete, onError)
        verify(onComplete, only()).invoke()
    }

    @Test
    fun subscribeIoHandleErrorSingleError() {
        presenter.testSubscribeIoHandleError(singleError, onSuccess, onError)
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }

    @Test
    fun subscribeIoHandleErrorCompletableError() {
        presenter.testSubscribeIo(completableError, onComplete, onError)
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }

    @Test
    fun subscribeIoHandleErrorMaybeError() {
        presenter.testSubscribeIoHandleError(maybeError, onSuccessMayBe, onComplete, onError)
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }
    //endregion

    //region subscribeAutoReload
    @Test
    fun subscribeIoAutoReloadSingle() {
        doReturn(true).`when`<ConnectionProvider>(connectionProvider).isDisconnected
        doReturn(Observable.just(true)).`when`<ConnectionProvider>(connectionProvider).observeConnectionChanges()

        presenter.testSubscribeIoAutoReload(singleError, reloadAction, onSuccess, onError)
        verify(reloadAction, only()).invoke()
    }

    @Test
    fun subscribeAutoReloadCompletable() {
        doReturn(true).`when`<ConnectionProvider>(connectionProvider).isDisconnected
        doReturn(Observable.just(true)).`when`<ConnectionProvider>(connectionProvider).observeConnectionChanges()

        presenter.testSubscribeIoAutoReload(completableError, reloadAction, onComplete, onError)
        verify(reloadAction, only()).invoke()
    }

    @Test
    fun subscribeAutoReloadMaybe() {
        doReturn(true).`when`<ConnectionProvider>(connectionProvider).isDisconnected
        doReturn(Observable.just(true)).`when`<ConnectionProvider>(connectionProvider).observeConnectionChanges()

        presenter.testSubscribeIoAutoReload(maybeError, reloadAction, onSuccess, onComplete, onError)
        verify(reloadAction, only()).invoke()
    }
    //endregion

    //region subscribeAutoReloadHandleError
    @Test
    fun subscribeIoHandleErrorAutoReloadSingle() {
        doReturn(true).`when`<ConnectionProvider>(connectionProvider).isDisconnected
        doReturn(Observable.just(true)).`when`<ConnectionProvider>(connectionProvider).observeConnectionChanges()

        presenter.testSubscribeIoHandleErrorAutoReload(singleError, reloadAction, onSuccess, onError)
        verify(reloadAction, only()).invoke()
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }

    @Test
    fun subscribeHandleErrorAutoReloadCompletable() {
        doReturn(true).`when`<ConnectionProvider>(connectionProvider).isDisconnected
        doReturn(Observable.just(true)).`when`<ConnectionProvider>(connectionProvider).observeConnectionChanges()

        presenter.testSubscribeIoHandleErrorAutoReload(completableError, reloadAction, onComplete, onError)
        verify(reloadAction, only()).invoke()
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }

    @Test
    fun subscribeHandleErrorAutoReloadMaybe() {
        doReturn(true).`when`<ConnectionProvider>(connectionProvider).isDisconnected
        doReturn(Observable.just(true)).`when`<ConnectionProvider>(connectionProvider).observeConnectionChanges()

        presenter.testSubscribeIoHandleErrorAutoReload(maybeError, reloadAction, onSuccess, onComplete, onError)
        verify(reloadAction, only()).invoke()
        verify<(Throwable) -> Unit>(onError, only()).invoke(testThrowable)
    }
    //endregion
}
