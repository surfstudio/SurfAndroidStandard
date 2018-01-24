package ru.surfstudio.android.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;
import ru.surfstudio.android.core.app.interactor.common.DataStrategy;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.util.rx.SafeFunction;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.CacheEmptyException;
import ru.surfstudio.android.network.error.NotModifiedException;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static ru.surfstudio.android.network.BaseNetworkInteractorTest.Response.CACHE;
import static ru.surfstudio.android.network.BaseNetworkInteractorTest.Response.SERVER_QM_FORCE;
import static ru.surfstudio.android.network.BaseNetworkInteractorTest.Response.SERVER_QM_ONLY_IF_CHANGED;
import static ru.surfstudio.android.network.BaseNetworkInteractorTest.Response.SERVER_QM_SIMPLE_CACHE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_FORCE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_ONLY_IF_CHANGED;

/**
 * unit tests for {@link BaseNetworkInteractor}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class BaseNetworkInteractorTest {
    @Mock
    private ConnectionQualityProvider qualityProvider;

    private BaseNetworkInteractor repository;
    private Single<Response> cacheRequest;
    private SafeFunction<Integer, Single<Response>> queryResolver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Logger.class);

        cacheRequest = Single.just(CACHE);
        repository = new BaseNetworkInteractor(qualityProvider);
        queryResolver = integer -> {
            switch (integer) {
                case QUERY_MODE_ONLY_IF_CHANGED:
                    return Single.just(SERVER_QM_ONLY_IF_CHANGED);
                case QUERY_MODE_FORCE:
                    return Single.just(SERVER_QM_FORCE);
                case QUERY_MODE_FROM_SIMPLE_CACHE:
                    return Single.just(SERVER_QM_SIMPLE_CACHE);
                default:
                    throw new IllegalStateException();
            }
        };

        doReturn(true).when(qualityProvider).isConnectedFast();
    }

    @Test
    public void testFromCache() throws Exception {
        repository.hybridQuery(DataStrategy.CACHE, cacheRequest, queryResolver)
                .test()
                .assertValues(CACHE, SERVER_QM_ONLY_IF_CHANGED);
    }

    @Test
    public void testFromServer() {
        repository.hybridQuery(DataStrategy.SERVER, cacheRequest, queryResolver)
                .test().assertValues(SERVER_QM_ONLY_IF_CHANGED);
    }

    @Test
    public void testNonActualFastConnection() throws Exception {
        queryResolver = integer -> Single.error(new NotModifiedException(new IllegalArgumentException(), 300, "http://ya.ru"));
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, queryResolver)
                .test()
                .assertValues(CACHE);
    }

    @Test
    public void testActualFastConnection() throws Exception {
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, queryResolver)
                .test()
                .assertValues(SERVER_QM_ONLY_IF_CHANGED);
    }

    @Test
    public void testActualSlowConnection() throws Exception {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, queryResolver)
                .test()
                .assertValues(SERVER_QM_ONLY_IF_CHANGED);
    }

    @Test
    public void testActualNonModified() throws Exception {
        queryResolver = integer -> Single.error(new NotModifiedException(new TestServerException(), 1, ""));

        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, queryResolver)
                .test()
                .assertValues(CACHE);

        doReturn(true).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, queryResolver)
                .test()
                .assertValues(CACHE);
    }

    @Test
    public void testAutoFastConnection() {
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, queryResolver)
                .test()
                .assertValues(SERVER_QM_ONLY_IF_CHANGED);
    }

    @Test
    public void testAutoSlowConnection() {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, queryResolver)
                .test()
                .assertValues(CACHE, SERVER_QM_ONLY_IF_CHANGED);
    }

    @Test
    public void testException() {
        cacheRequest = Single.error(new TestCacheException());
        queryResolver = integer -> Single.error(new TestServerException());
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, queryResolver)
                .test()
                .assertError(new TestServerException());
    }

    @Test
    public void testCacheException() {
        cacheRequest = Single.error(new TestCacheException());
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, queryResolver)
                .test()
                .assertNoErrors() // ошибка проглатывается, но логгируется
                .assertValues(SERVER_QM_FORCE);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Logger.e((Throwable) anyObject(), anyString());
    }

    @Test
    public void testEmptyCacheException() {
        cacheRequest = Single.error(new CacheEmptyException());
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, queryResolver)
                .test()
                .assertNoErrors() // ошибка проглатывается, но логгируется
                .assertValues(SERVER_QM_FORCE);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Logger.e((Throwable) anyObject(), anyString());
    }

    enum Response {
        SERVER_QM_ONLY_IF_CHANGED,
        SERVER_QM_FORCE,
        SERVER_QM_SIMPLE_CACHE,
        CACHE
    }

    private class TestServerException extends Exception {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestServerException;
        }
    }

    private class TestCacheException extends Exception {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestCacheException;
        }
    }

}