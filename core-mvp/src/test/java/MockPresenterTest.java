import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.mvp.presenter.BasePresenter;
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency;
import ru.surfstudio.android.core.ui.event.BaseScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.state.ActivityScreenState;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.rx.extension.ActionSafe;
import ru.surfstudio.android.rx.extension.ConsumerSafe;
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * unit tests for {@link BasePresenter}
 **/

@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class MockPresenterTest {

    @Mock
    ConnectionProvider connectionProvider;
    @Mock
    BasePresenterDependency basePresenterDependency;

    MockPresenter presenter;

    private Single<String> single = Single.just("Single");
    private Maybe<String> maybe = Maybe.just("Maybe");
    private Maybe maybeAction = Maybe.fromAction(() -> {
        int i = 1;
    });
    private Completable completable = Completable.complete();

    private Throwable testThrowable = new Throwable("test");
    private Single<String> singleError = Single.error(testThrowable);
    private Maybe<String> maybeError = Maybe.error(testThrowable);
    private Completable completableError = Completable.error(testThrowable);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Logger.class);

        SchedulersProvider schedulersProvider = new SchedulersProvider() {
            @Override
            public Scheduler main() {
                return Schedulers.trampoline(); //аналог immediate() в RxJava 2
            }

            @Override
            public Scheduler worker() {
                return Schedulers.trampoline(); //аналог immediate() в RxJava 2
            }
        };

        basePresenterDependency = new BasePresenterDependency(schedulersProvider,
                mock(ActivityScreenState.class),
                mock(BaseScreenEventDelegateManager.class),
                mock(ErrorHandler.class),
                connectionProvider,
                mock(ActivityNavigator.class));
        presenter = new MockPresenter(basePresenterDependency);
    }

    //region subscribeIoTests
    @Test
    public void subscribeSingle() {

        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(single, onSuccess, onError);
        verify(onSuccess, only()).accept("Single");
    }

    @Test
    public void subscribeCompletable() {
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(completable, onComplete, onError);
        verify(onComplete, only()).run();
    }

    @Test
    public void subscribeMaybe() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(maybe, onSuccess, onComplete, onError);
        verify(onSuccess, only()).accept("Maybe");
    }

    @Test
    public void subscribeMaybeAction() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(maybeAction, onSuccess, onComplete, onError);
        verify(onComplete, only()).run();
    }

    @Test
    public void subscribeSingleError() {

        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(singleError, onSuccess, onError);
        verify(onError, only()).accept(testThrowable);
    }

    @Test
    public void subscribeCompletableError() {
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(completableError, onComplete, onError);
        verify(onError, only()).accept(testThrowable);
    }

    @Test
    public void subscribeMaybeError() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(maybeError, onSuccess, onComplete, onError);
        verify(onError, only()).accept(testThrowable);
    }
    //endregion

    //region subscribeIoHandlerError
    @Test
    public void subscribeIoHandleErrorSingle() {

        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIoHandleError(single, onSuccess, onError);
        verify(onSuccess, only()).accept("Single");
    }

    @Test
    public void subscribeIoHandleErrorCompletable() {
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIoHandleError(completable, onComplete, onError);
        verify(onComplete, only()).run();
    }

    @Test
    public void subscribeIoHandleErrorMaybe() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIoHandleError(maybe, onSuccess, onComplete, onError);
        verify(onSuccess, only()).accept("Maybe");
    }

    @Test
    public void subscribeIoHandleErrorMaybeAction() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIoHandleError(maybeAction, onSuccess, onComplete, onError);
        verify(onComplete, only()).run();
    }

    @Test
    public void subscribeIoHandleErrorSingleError() {

        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIoHandleError(singleError, onSuccess, onError);
        verify(onError, only()).accept(testThrowable);
    }

    @Test
    public void subscribeIoHandleErrorCompletableError() {
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIo(completableError, onComplete, onError);
        verify(onError, only()).accept(testThrowable);
    }

    @Test
    public void subscribeIoHandleErrorMaybeError() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);

        presenter.subscribeIoHandleError(maybeError, onSuccess, onComplete, onError);
        verify(onError, only()).accept(testThrowable);
    }
    //endregion

    //region subscribeAutoReload
    @Test
    public void subscribeIoAutoReloadSingle() {

        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);
        ActionSafe reloadAction = mock(ActionSafe.class);

        doReturn(true).when(connectionProvider).isDisconnected();
        doReturn(Observable.just(true)).when(connectionProvider).observeConnectionChanges();

        presenter.subscribeIoAutoReload(singleError, reloadAction, onSuccess, onError);
        verify(reloadAction, only()).run();
    }

    @Test
    public void subscribeAutoReloadCompletable() {
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);
        ActionSafe reloadAction = mock(ActionSafe.class);

        doReturn(true).when(connectionProvider).isDisconnected();
        doReturn(Observable.just(true)).when(connectionProvider).observeConnectionChanges();

        presenter.subscribeIoAutoReload(completableError, reloadAction, onComplete, onError);
        verify(reloadAction, only()).run();
    }

    @Test
    public void subscribeAutoReloadMaybe() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);
        ActionSafe reloadAction = mock(ActionSafe.class);

        doReturn(true).when(connectionProvider).isDisconnected();
        doReturn(Observable.just(true)).when(connectionProvider).observeConnectionChanges();

        presenter.subscribeIoAutoReload(maybeError, reloadAction, onSuccess, onComplete, onError);
        verify(reloadAction, only()).run();
    }
    //endregion

    //region subscribeAutoReloadHandleError
    @Test
    public void subscribeIoHandleErrorAutoReloadSingle() {

        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);
        ActionSafe reloadAction = mock(ActionSafe.class);

        doReturn(true).when(connectionProvider).isDisconnected();
        doReturn(Observable.just(true)).when(connectionProvider).observeConnectionChanges();

        presenter.subscribeIoHandleErrorAutoReload(singleError, reloadAction, onSuccess, onError);
        verify(reloadAction, only()).run();
        verify(onError, only()).accept(testThrowable);
    }

    @Test
    public void subscribeHandleErrorAutoReloadCompletable() {
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);
        ActionSafe reloadAction = mock(ActionSafe.class);

        doReturn(true).when(connectionProvider).isDisconnected();
        doReturn(Observable.just(true)).when(connectionProvider).observeConnectionChanges();

        presenter.subscribeIoHandleErrorAutoReload(completableError, reloadAction, onComplete, onError);
        verify(reloadAction, only()).run();
        verify(onError, only()).accept(testThrowable);
    }

    @Test
    public void subscribeHandleErrorAutoReloadMaybe() {
        ConsumerSafe<String> onSuccess = mock(ConsumerSafe.class);
        ActionSafe onComplete = mock(ActionSafe.class);
        ConsumerSafe<Throwable> onError = mock(ConsumerSafe.class);
        ActionSafe reloadAction = mock(ActionSafe.class);

        doReturn(true).when(connectionProvider).isDisconnected();
        doReturn(Observable.just(true)).when(connectionProvider).observeConnectionChanges();

        presenter.subscribeIoHandleErrorAutoReload(maybeError, reloadAction, onSuccess, onComplete, onError);
        verify(reloadAction, only()).run();
        verify(onError, only()).accept(testThrowable);
    }
    //endregion
}
