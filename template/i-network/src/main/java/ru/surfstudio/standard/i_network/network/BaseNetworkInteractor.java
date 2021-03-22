/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.standard.i_network.network;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.rx.extension.FunctionSafe;
import ru.surfstudio.standard.i_network.network.error.NotModifiedException;

/**
 * Базовый класс репозитория
 * Можно переопределить DataStrategy по умолчанию в классе {@link DataStrategy}
 */
public class BaseNetworkInteractor {

    private ConnectionProvider connectionProvider;

    public BaseNetworkInteractor(ConnectionProvider connectionQualityProvider) {
        this.connectionProvider = connectionQualityProvider;
    }

    /**
     * Действия при смене пользователя
     */
    public void onSessionChanged() {
        //default empty
    }

    protected <T> Observable<T> hybridQuery(DataStrategy priority,
                                            Single<T> cacheRequest,
                                            FunctionSafe<Integer, Single<T>> networkRequestCreator) {
        return hybridQuery(priority, cacheRequest.toObservable(), integer -> networkRequestCreator.apply(integer).toObservable());
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
    protected <T> Observable<T> hybridQuery(DataStrategy priority,
                                            Observable<T> cacheRequest,
                                            FunctionSafe<Integer, Observable<T>> networkRequestCreator) {
        return cacheRequest
                //оборачиваем ошибку получения кеша чтобы обработать ее в конце чейна
                .onErrorResumeNext((Throwable e) -> Observable.error(new CacheExceptionWrapper(e)))
                .flatMap(cache -> {
                    @BaseServerConstants.QueryMode int queryMode = BaseServerConstants.QUERY_MODE_ONLY_IF_CHANGED;
                    boolean cacheFirst = priority == DataStrategy.AUTO
                            ? !connectionProvider.isConnectedFast()
                            : priority == DataStrategy.CACHE;
                    boolean onlyActual = priority == DataStrategy.ONLY_ACTUAL;
                    Observable<T> cacheResultObservable = Observable.just(cache);
                    Observable<T> networkRequestObservable = networkRequestCreator.apply(queryMode);

                    return getDataObservable(cacheFirst, onlyActual, cacheResultObservable, networkRequestObservable);
                })
                .onErrorResumeNext((Throwable throwable) ->
                        this.processErrorFinal(
                                throwable,
                                networkRequestCreator.apply(BaseServerConstants.QUERY_MODE_FORCE)));
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
        return hybridQuery(DataStrategy.AUTO, cacheRequest, networkRequestCreator);
    }

    protected <T> Observable<T> hybridQuery(Single<T> cacheRequest,
                                            FunctionSafe<Integer, Single<T>> networkRequestCreator) {
        return hybridQuery(DataStrategy.AUTO, cacheRequest, networkRequestCreator);
    }

    /**
     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
     *
     * @param requestCreator функция, которая должна вернуть запрос к серверу, поддерживающий механизм простого кеширования
     *                       Integer параметр этой функции определяет {@link BaseServerConstants.QueryMode}
     * @param <T>            тип ответа сервера
     */
    protected <T> Observable<T> hybridQueryWithSimpleCache(DataStrategy priority,
                                                           FunctionSafe<Integer, Observable<T>> requestCreator) {
        return hybridQuery(priority, requestCreator.apply(BaseServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE), requestCreator);
    }

    protected <T> Observable<T> hybridQueryWithSimpleCache(FunctionSafe<Integer, Observable<T>> requestCreator) {
        return hybridQueryWithSimpleCache(DataStrategy.DEFAULT_DATA_STRATEGY, requestCreator);
    }

    protected <T> Observable<T> hybridQueryWithSimpleCacheForSingle(DataStrategy priority,
                                                                    FunctionSafe<Integer, Single<T>> requestCreator) {
        return hybridQuery(priority, requestCreator.apply(BaseServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE), requestCreator);
    }

    protected <T> Observable<T> hybridQueryWithSimpleCacheForSingle(FunctionSafe<Integer, Single<T>> requestCreator) {
        return hybridQueryWithSimpleCacheForSingle(DataStrategy.DEFAULT_DATA_STRATEGY, requestCreator);
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
