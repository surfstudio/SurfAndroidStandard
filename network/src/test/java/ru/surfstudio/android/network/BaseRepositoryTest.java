package ru.surfstudio.android.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Maybe;
import io.reactivex.Single;
import ru.surfstudio.android.core.app.interactor.common.DataPriority;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.NotModifiedException;

import static org.mockito.Mockito.doReturn;
import static ru.surfstudio.android.network.BaseRepositoryTest.Response.CACHE;
import static ru.surfstudio.android.network.BaseRepositoryTest.Response.SERVER;

/**
 * unit tests for {@link BaseRepository}
 */
public class BaseRepositoryTest {
    @Mock
    ConnectionQualityProvider qualityProvider;

    private BaseRepository repository;
    private Maybe<Response> cacheRequest;
    private Single<Response> networkRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        networkRequest = Single.just(SERVER);
        cacheRequest = Maybe.just(CACHE);
        repository = new BaseRepository(qualityProvider);

        doReturn(true).when(qualityProvider).isConnectedFast();
    }

    @Test
    public void testFromCache() throws Exception {
        repository.hybridQuery(DataPriority.CACHE, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testFromServer() {
        repository.hybridQuery(DataPriority.SERVER, cacheRequest, integer -> networkRequest)
                .test().assertValues(SERVER);
    }

    @Test
    public void testNonActualFastConnection() throws Exception {
        networkRequest = Single.error(new NotModifiedException(new IllegalArgumentException(), 300, "http://ya.ru"));
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE);
    }

    @Test
    public void testActualFastConnection() throws Exception {
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testActualSlowConnection() throws Exception {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testAutoFastConnection() {
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testAutoSlowConnection() {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testException() {
        cacheRequest = Maybe.error(new TestCacheException());
        networkRequest = Single.error(new TestServerException());
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertError(new TestServerException());
    }

    @Test
    public void testCacheException() {
        cacheRequest = Maybe.error(new TestCacheException());
        networkRequest = Single.error(new TestServerException());
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertError(new TestServerException())
                .assertValues(); // должны ли мы возвращать что-то с сервера в этом случае?
    }

    enum Response {
        SERVER, CACHE
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