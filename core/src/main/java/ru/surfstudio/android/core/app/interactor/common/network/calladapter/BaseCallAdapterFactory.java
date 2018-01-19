package ru.surfstudio.android.core.app.interactor.common.network.calladapter;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import ru.surfstudio.android.core.app.interactor.common.network.error.CacheEmptyException;
import ru.surfstudio.android.core.app.interactor.common.network.error.NoInternetException;
import rx.Observable;

/**
 * кроме конвертирования запроса в Observable, выполняет следующие функции:
 * Конвертирует IOException в NoConnectionException
 * Имплементация провайдится через модуль со скоупом {@link ru.surfstudio.android.core.app.dagger.scope.PerApplication}
 */
public abstract class BaseCallAdapterFactory extends CallAdapter.Factory {

    private RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    @SuppressWarnings("unchecked")
    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter<Observable<?>> rxCallAdapter =
                (CallAdapter<Observable<?>>) rxJavaCallAdapterFactory.get(returnType, annotations, retrofit);
        return new ResultCallAdapter(rxCallAdapter, returnType);
    }

    /**
     * Метод обработки ошибки {@link HttpException}
     * Здесь определеяется поведение на различные коды ошибок. Например:
     *      * c кодом 401 и если пользователь был авторизован - сбрасывает все данные пользователя и открывает экран авторизации
     *      * c кодом 400 перезапрашивает токен и повторяет предыдущий запрос
     */
    abstract <R> Observable<R> onHttpException(HttpException e);

    private final class ResultCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final CallAdapter<Observable<?>> rxCallAdapter;

        protected ResultCallAdapter(CallAdapter<Observable<?>> rxCallAdapter, Type returnType) {
            Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
            this.rxCallAdapter = rxCallAdapter;
            this.responseType = observableType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            Observable<R> observable = (Observable<R>) rxCallAdapter.adapt(call);
            return observable.onErrorResumeNext(this::handleNetworkError);
        }

        private <R> Observable<R> handleNetworkError(Throwable e) {
            if (e instanceof IOException) {
                return Observable.error(new NoInternetException(e));
            } else if (e instanceof CacheEmptyException) {
                return Observable.just(null); //кеш пуст
            } else if (e instanceof HttpException) {
                return onHttpException((HttpException) e);
            } else {
                return Observable.error(e);
            }
        }
    }
}
