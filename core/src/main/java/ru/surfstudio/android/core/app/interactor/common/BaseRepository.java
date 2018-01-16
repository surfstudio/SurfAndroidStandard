//package ru.surfstudio.android.core.app.interactor.common;
//
//import ru.surfstudio.android.core.app.interactor.common.network.ServerConstants;
//import ru.surfstudio.android.core.app.log.Logger;
//import rx.Observable;
//import rx.functions.Func1;
//
//import static ru.surfstudio.android.core.app.interactor.common.network.ServerConstants.QUERY_MODE_FORCE;
//import static ru.surfstudio.android.core.app.interactor.common.network.ServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;
//import static ru.surfstudio.android.core.app.interactor.common.network.ServerConstants.QUERY_MODE_ONLY_IF_CHANGED;
//
//
///**
// * Базовый класс репозитория
// */
//public class BaseRepository {
//
//    private ConnectionQualityProvider connectionQualityProvider;
//
//    public BaseRepository(ConnectionQualityProvider connectionQualityProvider) {
//        this.connectionQualityProvider = connectionQualityProvider;
//    }
//
//    /**
//     * Действия при смене пользователя
//     */
//    public void onSessionChanged() {
//        //default empty
//    }
//
//    /**
//     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
//     *
//     * @param priority              указывает из какого источника данные должны эмитится в результирующий
//     *                              Observable в первую очередь
//     * @param cacheRequest          запрос к кешу
//     * @param networkRequestCreator функция, которая должна вернуть запрос к серверу,
//     *                              Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
//     * @param <T>                   тип возвращаемого значения
//     */
//    protected <T> Observable<T> hybridQuery(DataPriority priority,
//                                            Observable<T> cacheRequest,
//                                            Func1<Integer, Observable<T>> networkRequestCreator) {
//        return cacheRequest
//                //оборачиваем ошибку получения кеша чтобы обработать ее в конце чейна
//                .onErrorResumeNext(e -> Observable.error(new CacheExceptionWrapper(e)))
//                .flatMap(cache -> {
//                    boolean cacheExist = cache != null;
//                    @ServerConstants.QueryMode int queryMode = cacheExist
//                            ? QUERY_MODE_ONLY_IF_CHANGED
//                            : QUERY_MODE_FORCE;
//                    boolean cacheFirst = priority == DataPriority.AUTO
//                            ? !connectionQualityProvider.isConnectedFast()
//                            : priority == DataPriority.CACHE;
//                    Observable<T> cacheResultObservable = cacheExist ? Observable.just(cache) : Observable.empty();
//                    Observable<T> networkRequestObservable = networkRequestCreator.call(queryMode);
//
//                    if (cacheFirst) {
//                        return Observable.concat(
//                                cacheResultObservable,
//                                networkRequestObservable.onErrorResumeNext(
//                                        this::processNetworkException));
//                    } else {
//                        return networkRequestObservable.onErrorResumeNext(e ->
//                                Observable.concat(
//                                        cacheResultObservable,
//                                        processNetworkException(e)));
//                    }
//                })
//                .onErrorResumeNext(e -> {
//                    if(e instanceof CacheExceptionWrapper) {
//                        //в случае ошибки получения данных из кеша производим запрос на сервер
//                        Logger.e(e.getCause(), "Error when getting data from cache");
//                        return networkRequestCreator.call(QUERY_MODE_FORCE);
//                    } else {
//                        return Observable.error(e);
//                    }
//                });
//    }
//
//    protected <T> Observable<T> hybridQuery(Observable<T> cacheRequest,
//                                            Func1<Integer, Observable<T>> networkRequestCreator) {
//        return hybridQuery(DataPriority.AUTO, cacheRequest, networkRequestCreator);
//    }
//
//    /**
//     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
//     *
//     * @param requestCreator функция, которая должна вернуть запрос к серверу, поддерживающий механизм простого кеширования
//     *                       Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
//     * @param <T>            тип ответа сервера
//     */
//    protected <T> Observable<T> hybridQueryWithSimpleCache(DataPriority priority,
//                                                                                Func1<Integer, Observable<T>> requestCreator) {
//        return hybridQuery(priority, requestCreator.call(QUERY_MODE_FROM_SIMPLE_CACHE), requestCreator);
//    }
//
//    protected <T> Observable<T> hybridQueryWithSimpleCache(Func1<Integer, Observable<T>> requestCreator) {
//        return hybridQueryWithSimpleCache(DataPriority.AUTO, requestCreator);
//    }
//
//    private <T> Observable<T> processNetworkException(Throwable e) {
//        return e instanceof NotModifiedException
//                ? Observable.empty()
//                : Observable.error(e);
//    }
//
//    /**
//     * оборачивает ошибку, полученную при получении данных из кеша
//     */
//    private class CacheExceptionWrapper extends Exception {
//        public CacheExceptionWrapper(Throwable cause) {
//            super(cause);
//        }
//    }
//}
