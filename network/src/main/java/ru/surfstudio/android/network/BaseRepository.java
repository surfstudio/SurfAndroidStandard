package ru.surfstudio.android.network;

import com.annimon.stream.function.Function;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import ru.surfstudio.android.core.app.interactor.common.DataPriority;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.NotModifiedException;

import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_FORCE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;
import static ru.surfstudio.android.network.ServerConstants.QUERY_MODE_ONLY_IF_CHANGED;


/**
 * Базовый класс репозитория
 */
public class BaseRepository {

    private ConnectionQualityProvider connectionQualityProvider;

    public BaseRepository(ConnectionQualityProvider connectionQualityProvider) {
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
     *                              Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
     * @param <T>                   тип возвращаемого значения
     */
    protected <T> Observable<T> hybridQuery(DataPriority priority,
                                            Maybe<T> cacheRequest,
                                            Function<Integer, Single<T>> networkRequestCreator) {
        return cacheRequest
                .switchIfEmpty(networkRequestCreator.apply(QUERY_MODE_FORCE)) //в случае ошибки получения данных из кеша производим запрос на сервер
                .flatMapObservable(cache -> {
                    @ServerConstants.QueryMode int queryMode = QUERY_MODE_ONLY_IF_CHANGED;
                    boolean cacheFirst = priority == DataPriority.AUTO
                            ? !connectionQualityProvider.isConnectedFast()
                            : priority == DataPriority.CACHE;
                    boolean onlyActual = priority == DataPriority.ONLY_ACTUAL;
                    Single<T> cacheResultObservable = Single.just(cache);
                    Single<T> networkRequestObservable = networkRequestCreator.apply(queryMode);

                    return getDataObservable(cacheFirst, onlyActual, cacheResultObservable, networkRequestObservable);
                });
    }

    private <T> Observable<T> getDataObservable(boolean cacheFirst, boolean onlyActual, Single<T> cacheResultObservable,
                                                Single<T> networkRequestObservable) {
        if (cacheFirst) {
            return Single.concat(cacheResultObservable, networkRequestObservable)
                    .toObservable()
                    .onErrorResumeNext((Throwable e) -> processNetworkException(e));
        } else if (onlyActual) {
            return networkRequestObservable.onErrorResumeNext(e -> e instanceof NotModifiedException ?
                    cacheResultObservable :
                    Single.error(e))
                    .toObservable();
        } else {
            return Single.concat(networkRequestObservable, cacheResultObservable)
                    .toObservable()
                    .onErrorResumeNext((Throwable e) -> processNetworkException(e));
        }
    }

    private <T> ObservableSource<? extends T> processNetworkException(Observer<? super T> observer) {
        return null;
    }

    protected <T> Observable<T> hybridQuery(Maybe<T> cacheRequest,
                                            Function<Integer, Single<T>> networkRequestCreator) {
        return hybridQuery(DataPriority.AUTO, cacheRequest, networkRequestCreator);
    }

    /**
     * Осуществляет гибридный запрос, в методе происходит объединение данных приходящих с сервера и из кеша
     *
     * @param requestCreator функция, которая должна вернуть запрос к серверу, поддерживающий механизм простого кеширования
     *                       Integer параметр этой функции определяет {@link ServerConstants.QueryMode}
     * @param <T>            тип ответа сервера
     */
    protected <T> Observable<T> hybridQueryWithSimpleCache(DataPriority priority,
                                                           Function<Integer, Single<T>> requestCreator) {
        return hybridQuery(priority, requestCreator.apply(QUERY_MODE_FROM_SIMPLE_CACHE).toMaybe(), requestCreator);
    }

    protected <T> Observable<T> hybridQueryWithSimpleCache(Function<Integer, Single<T>> requestCreator) {
        return hybridQueryWithSimpleCache(DataPriority.AUTO, requestCreator);
    }

    private <T> ObservableSource<? extends T> processNetworkException(Throwable e) {
        return e instanceof NotModifiedException
                ? Observable.empty()
                : Observable.error(e);
    }
}
