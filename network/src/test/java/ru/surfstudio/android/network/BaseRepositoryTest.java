package ru.surfstudio.android.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.surfstudio.android.core.app.interactor.common.DataPriority;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.NotModifiedException;
import rx.Observable;
import rx.observers.TestSubscriber;

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
    private Observable<Response> cacheRequest;
    private Observable<Response> networkRequest;
    private TestSubscriber<Response> subscriber;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        networkRequest = Observable.just(SERVER);
        cacheRequest = Observable.just(CACHE);
        repository = new BaseRepository(qualityProvider);
        subscriber = new TestSubscriber<>();

        doReturn(true).when(qualityProvider).isConnectedFast();
    }

    @Test
    public void testFromCache() throws Exception {
        repository.hybridQuery(DataPriority.CACHE, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(CACHE, SERVER);
    }

    @Test
    public void testFromServer() {
        subscriber = new TestSubscriber<>();
        repository.hybridQuery(DataPriority.SERVER, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(SERVER);
    }

    @Test
    public void testNonActualFastConnection() throws Exception {
        networkRequest = Observable.error(new NotModifiedException(new IllegalArgumentException(), 300, "http://ya.ru"));
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(CACHE);
    }

    @Test
    public void testActualFastConnection() throws Exception {
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(SERVER);
    }

    @Test
    public void testActualSlowConnection() throws Exception {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(SERVER);
    }

    @Test
    public void testAutoFastConnection() {
        subscriber = new TestSubscriber<>();
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(SERVER);
    }

    @Test
    public void testAutoSlowConnection() {
        doReturn(false).when(qualityProvider).isConnectedFast();
        subscriber = new TestSubscriber<>();
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertValues(CACHE, SERVER);
    }

    @Test
    public void testException() {
        cacheRequest = Observable.error(new TestCacheException());
        networkRequest = Observable.error(new TestServerException());
        subscriber = new TestSubscriber<>();
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertError(new TestServerException());
    }

    @Test
    public void testCacheException() {
        cacheRequest = Observable.error(new TestCacheException());
        networkRequest = Observable.error(new TestServerException());
        subscriber = new TestSubscriber<>();
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .subscribe(subscriber);
        subscriber.assertError(new TestServerException());
        subscriber.assertValues(); // должны ли мы возвращать что-то с сервера в этом случае?
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