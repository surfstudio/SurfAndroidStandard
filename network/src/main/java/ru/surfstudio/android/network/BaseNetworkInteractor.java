package ru.surfstudio.android.network;

import io.reactivex.Observable;
import ru.surfstudio.android.core.app.interactor.common.DataPriority;
import ru.surfstudio.android.core.util.rx.FunctionSafe;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.NotModifiedException;

import static ru.surfstudio.android.network.BaseServerConstants.QUERY_MODE_FORCE;
import static ru.surfstudio.android.network.BaseServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;
import static ru.surfstudio.android.network.BaseServerConstants.QUERY_MODE_ONLY_IF_CHANGED;


/**
 * Базовый класс репозитория
 */
public class BaseNetworkInteractor {

    private ConnectionQualityProvider connectionQualityProvider;

    public BaseNetworkInteractor(ConnectionQualityProvider connectionQualityProvider) {
        this.connectionQualityProvider = connectionQualityProvider;
    }

    /**
     * Действия при смене пользователя
     */
    public void onSessionChanged() {
        //default empty
    }

    /**
     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
     *
     * @param priority              указывает из какого источника данные должны эмитится в результирующий
     *                              Observable в первую очередь
     * @param cacheRequest          запрос к кешу
     * @param networkRequestCreator функция, которая должна вернуть запрос к серверу,
     *                              Integer параметр этой функции определяет {@link BaseServerConstants.QueryMode}
     * @param <T>                   тип возвращаемого значения
     */
    protected <T> Observable<T> hybridQuery(DataPriority priority,
                                            Observable<T> cacheRequest,
                                            FunctionSafe<Integer, Observable<T>> networkRequestCreator) {
        return cacheRequest
                //оборачиваем ошибку получения кеша чтобы обработать ее в конце чейна
                .onErrorResumeNext((Throwable e) -> Observable.error(new CacheExceptionWrapper(e)))
                .flatMap(cache -> {
                    @BaseServerConstants.QueryMode int queryMode = QUERY_MODE_ONLY_IF_CHANGED;
                    boolean cacheFirst = priority == DataPriority.AUTO
                            ? !connectionQualityProvider.isConnectedFast()
                            : priority == DataPriority.CACHE;
                    boolean onlyActual = priority == DataPriority.ONLY_ACTUAL;
                    Observable<T> cacheResultObservable = Observable.just(cache);
                    Observable<T> networkRequestObservable = networkRequestCreator.apply(queryMode);

                    return getDataObservable(cacheFirst, onlyActual, cacheResultObservable, networkRequestObservable);
                })
                .onErrorResumeNext((Throwable throwable) ->
                        this.processErrorFinal(
                                throwable,
                                networkRequestCreator.apply(QUERY_MODE_FORCE)));
    }

    private <T> Observable<T> getDataObservable(boolean cacheFirst, boolean onlyActual, Observable<T> cacheResultObservable,
                                                Observable<T> networkRequestObservable) {
        if (cacheFirst) {
            return Observable.concat(
                    cacheResultObservable,
                    networkRequestObservable.onErrorResumeNext((Throwable e) -> processNetworkException(e)));
        } else if (onlyActual) {
            return networkRequestObservable.onErrorResumeNext(e -> e instanceof NotModifiedException ?
                    cacheResultObservable :
                    Observable.error(e));
        } else {
            return networkRequestObservable.onErrorResumeNext((Throwable e) ->
                    Observable.concat(
                            cacheResultObservable,
                            processNetworkException(e)));
        }
    }

    private <T> Observable<T> processErrorFinal(Throwable e, Observable<T> networkRequest) {
        if (e instanceof CacheExceptionWrapper) {
            //в случае ошибки получения данных из кеша производим запрос на сервер
            Logger.e(e.getCause(), "Error when getting data from cache");
            return networkRequest;
        } else {
            return Observable.error(e);
        }
    }

    protected <T> Observable<T> hybridQuery(Observable<T> cacheRequest,
                                            FunctionSafe<Integer, Observable<T>> networkRequestCreator) {
        return hybridQuery(DataPriority.AUTO, cacheRequest, networkRequestCreator);
    }

    /**
     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
     *
     * @param requestCreator функция, которая должна вернуть запрос к серверу, поддерживающий механизм простого кеширования
     *                       Integer параметр этой функции определяет {@link BaseServerConstants.QueryMode}
     * @param <T>            тип ответа сервера
     */
    protected <T> Observable<T> hybridQueryWithSimpleCache(DataPriority priority,
                                                           FunctionSafe<Integer, Observable<T>> requestCreator) {
        return hybridQuery(priority, requestCreator.apply(QUERY_MODE_FROM_SIMPLE_CACHE), requestCreator);
    }

    protected <T> Observable<T> hybridQueryWithSimpleCache(FunctionSafe<Integer, Observable<T>> requestCreator) {
        return hybridQueryWithSimpleCache(DataPriority.AUTO, requestCreator);
    }

    private <T> Observable<T> processNetworkException(Throwable e) {
        return e instanceof NotModifiedException
                ? Observable.empty()
                : Observable.error(e);
    }

    /**
     * оборачивает ошибку, полученную при получении данных из кеша
     */
    private class CacheExceptionWrapper extends Exception {
        CacheExceptionWrapper(Throwable cause) {
            super(cause);
        }
    }
}
