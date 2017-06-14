package com.surf.myapplication.interactor.common;

import ru.labirint.android.app.log.Logger;
import ru.labirint.android.interactor.common.network.ServerConstants;
import ru.labirint.android.interactor.common.network.response.BaseResponse;
import rx.Observable;
import rx.functions.Func1;

import static ru.labirint.android.interactor.common.network.ServerConstants.QUERY_MODE_FORCE;
import static ru.labirint.android.interactor.common.network.ServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;
import static ru.labirint.android.interactor.common.network.ServerConstants.QUERY_MODE_ONLY_IF_CHANGED;
import static ru.labirint.android.interactor.common.network.ServerConstants.QueryMode;

/**
 * Базовый класс репозитория
 */
public class BaseRepository {

    /**
     * Осуществляет гибридный запрос, в методе происходит обьединение данных приходящих с сервера и из кеша
     *
     * @param cacheRequest          запрос к кешу
     * @param networkRequestCreator фукция, которя должна вернуть запрос к серверу,
     *                              Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
     * @param <T>                   тип возвращемого занчения
     */
    protected <T> Observable<T> hybridQuery(Observable<T> cacheRequest,
                                            Func1<Integer, Observable<T>> networkRequestCreator) {
        return cacheRequest
                .onErrorResumeNext(e -> {
                    Logger.e(e, "Error when getting data from cache");
                    return networkRequestCreator.call(QUERY_MODE_FORCE);
                })
                .flatMap(cache -> {
                    boolean cacheExist = cache != null;
                    @QueryMode int queryMode = cacheExist
                            ? QUERY_MODE_ONLY_IF_CHANGED
                            : QUERY_MODE_FORCE;
                    return Observable.concat(
                            cacheExist ? Observable.just(cache) : Observable.empty(),
                            networkRequestCreator.call(queryMode));
                });

    }

    /**
     * Осуществляет гибридный запрос, в методе происходит обьединение данных приходящих с сервера и из кеша
     *
     * @param requestCreator фукция, которя должна вернуть запрос к серверу, поддерживающий механизм простого кеширования
     *                       Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
     * @param <T>            тип ответа сервера
     */
    protected <T extends BaseResponse> Observable<T> hybridQueryWithSimpleCache(Func1<Integer, Observable<T>> requestCreator) {
        return hybridQuery(requestCreator.call(QUERY_MODE_FROM_SIMPLE_CACHE), requestCreator);
    }
}
