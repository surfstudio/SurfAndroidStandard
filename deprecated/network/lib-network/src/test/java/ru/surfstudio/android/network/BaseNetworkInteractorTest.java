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

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.network.error.CacheEmptyException;
import ru.surfstudio.android.network.error.NotModifiedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static ru.surfstudio.android.network.BaseNetworkInteractorTest.Response.CACHE;
import static ru.surfstudio.android.network.BaseNetworkInteractorTest.Response.SERVER;

/**
 * unit tests for {@link BaseNetworkInteractor}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class BaseNetworkInteractorTest {
    @Mock
    ConnectionProvider qualityProvider;

    private BaseNetworkInteractor repository;
    private Observable<Response> cacheRequest;
    private Observable<Response> networkRequest;

    private Single<Response> cacheSingleRequest;
    private Single<Response> networkSingleRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Logger.class);

        networkRequest = Observable.just(SERVER);
        cacheRequest = Observable.just(CACHE);

        networkSingleRequest = Single.just(SERVER);
        cacheSingleRequest = Single.just(CACHE);

        repository = new BaseNetworkInteractor(qualityProvider);

        doReturn(true).when(qualityProvider).isConnectedFast();
    }

    @Test
    public void testFromCache() throws Exception {
        repository.hybridQuery(DataStrategy.CACHE, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testFromServer() {
        repository.hybridQuery(DataStrategy.SERVER, cacheRequest, integer -> networkRequest)
                .test().assertValues(SERVER);
    }

    @Test
    public void testNonActualFastConnection() throws Exception {
        networkRequest = Observable.error(new NotModifiedException(new IllegalArgumentException(), 300, "http://ya.ru"));
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE);
    }

    @Test
    public void testActualFastConnection() throws Exception {
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testActualSlowConnection() throws Exception {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataStrategy.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testAutoFastConnection() {
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testAutoSlowConnection() {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testException() {
        cacheRequest = Observable.error(new TestCacheException());
        networkRequest = Observable.error(new TestServerException());
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertError(new TestServerException());
    }

    @Test
    public void testCacheException() {
        cacheRequest = Observable.error(new TestCacheException());
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertNoErrors() // ошибка проглатывается, но логгируется
                .assertValues(SERVER);
        PowerMockito.verifyStatic(Logger.class, VerificationModeFactory.times(1));
        Logger.w((Throwable) any(), anyString());
    }

    @Test
    public void testEmptyCacheException() {
        cacheRequest = Observable.error(new CacheEmptyException());
        repository.hybridQuery(DataStrategy.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testFromSingleCache() throws Exception {
        repository.hybridQuery(DataStrategy.CACHE, cacheSingleRequest, integer -> networkSingleRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testFromSingleServer() {
        repository.hybridQuery(DataStrategy.SERVER, cacheSingleRequest, integer -> networkSingleRequest)
                .test().assertValues(SERVER);
    }

    enum Response {
        SERVER, CACHE
    }

    private static class TestServerException extends Exception {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestServerException;
        }
    }

    private static class TestCacheException extends Exception {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestCacheException;
        }
    }

}