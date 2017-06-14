package com.surf.myapplication.interactor.location;

import android.support.annotation.WorkerThread;

import com.agna.ferro.mvp.component.scope.PerApplication;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import ru.labirint.android.interactor.location.error.LocationSecurityException;
import ru.labirint.android.interactor.location.error.LocationServiceUnavailableException;
import ru.labirint.android.interactor.scheduler.SchedulersProvider;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Сервис предоставляющий доступ к локации пользователя
 */
@PerApplication
public class LocationServiceImpl implements LocationService {

    private BehaviorSubject<LocationData> locationSubject = BehaviorSubject.create();
    private final LocationProvider locationProvider;
    private SchedulersProvider schedulersProvider;
    private volatile int numActiveLocationRequests = 0;

    @Inject
    public LocationServiceImpl(LocationProvider locationProvider, SchedulersProvider schedulersProvider) {
        this.locationProvider = locationProvider;
        this.schedulersProvider = schedulersProvider;
    }


    /**
     * запускает получение локации в фоне
     */
    @WorkerThread
    public void startGetLocationInBackground(long timeoutMs) {
        createLocationDataObservable(timeoutMs)//запускаем таймер на получение локации
                .subscribeOn(schedulersProvider.worker())
                .subscribe(
                        location -> {/*ignore*/},
                        e -> reset(),
                        () -> reset());
    }

    private synchronized Observable<LocationData> createLocationDataObservable(long timeoutMs) {
        Observable<LocationData> resultObservable;
        if (numActiveLocationRequests == 0) {
            resultObservable = Observable.fromCallable(() -> {
                locationProvider.getLocation(new LocationProviderListener());
                return null;
            }).flatMap(o -> locationSubject);
        } else {
            resultObservable = locationSubject;
        }
        numActiveLocationRequests++;
        return resultObservable
                .first()
                .timeout(timeoutMs, TimeUnit.MILLISECONDS);
    }

    /**
     * @return observable, который эмитит локацию пользователя один раз
     * может также эмитит ошибки: {@link LocationServiceUnavailableException},
     * {@link TimeoutException}, {@link LocationSecurityException}
     * <p>
     * Если ранее был запрос на локацию и он не завершился, то старый запрос переиспользуется
     */
    @Override
    public Observable<LocationData> getLocation(long timeoutMs) {
        return createLocationDataObservable(timeoutMs)
                .doOnUnsubscribe(() -> reset());
    }

    /**
     * отменяет текущий запрос и подготавливает сервис к новому запросу
     */
    private synchronized void reset() {
        numActiveLocationRequests--;
        if (numActiveLocationRequests == 0) {
            locationProvider.reset();
            locationSubject = BehaviorSubject.create();
        }
    }

    private class LocationProviderListener implements LocationProvider.LocationListener {

        @Override
        public void onSuccess(LocationData locationData) {
            locationSubject.onNext(locationData);
        }

        @Override
        public void onError(Throwable e) {
            locationSubject.onError(e);
        }
    }
}
