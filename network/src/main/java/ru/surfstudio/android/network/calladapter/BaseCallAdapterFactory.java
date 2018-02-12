package ru.surfstudio.android.network.calladapter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import ru.surfstudio.android.dagger.scope.PerApplication;
import ru.surfstudio.android.network.error.NoInternetException;

/**
 * кроме конвертирования запроса в Observable, выполняет следующие функции:
 * Конвертирует IOException в NoConnectionException
 * Имплементация провайдится через модуль со скоупом {@link PerApplication}
 */
public abstract class BaseCallAdapterFactory extends CallAdapter.Factory {

    private RxJava2CallAdapterFactory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    @SuppressWarnings("unchecked")
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter<Observable<?>, Observable<?>> rxCallAdapter =
                (CallAdapter<Observable<?>, Observable<?>>) rxJavaCallAdapterFactory.get(returnType, annotations, retrofit);
        return new ResultCallAdapter(rxCallAdapter, returnType);
    }

    /**
     * Метод обработки ошибки {@link HttpException}
     * Здесь определеяется поведение на различные коды ошибок. Например:
     *      * c кодом 401 и если пользователь был авторизован - сбрасывает все данные пользователя и открывает экран авторизации
     *      * c кодом 400 перезапрашивает токен и повторяет предыдущий запрос
     */
    public abstract <R> Observable<R> onHttpException(HttpException e);

    private final class ResultCallAdapter implements CallAdapter<Observable<?>, Observable<?>> {
        private final Type responseType;
        private final CallAdapter<Observable<?>, Observable<?>> rxCallAdapter;

        protected ResultCallAdapter(CallAdapter<Observable<?>, Observable<?>> rxCallAdapter, Type returnType) {
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
        public Observable<?> adapt(Call<Observable<?>> call) {
            Observable<?> observable = rxCallAdapter.adapt(call);
            return observable.onErrorResumeNext((Throwable e) -> handleNetworkError(e));
        }

        private <R> Observable<R> handleNetworkError(Throwable e) {
            if (e instanceof IOException) {
                return Observable.error(new NoInternetException(e));
            } else if (e instanceof HttpException) {
                return onHttpException((HttpException) e);
            } else {
                return Observable.error(e);
            }
        }
    }
}
