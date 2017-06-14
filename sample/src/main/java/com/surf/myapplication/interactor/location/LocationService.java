package com.surf.myapplication.interactor.location;

import java.util.concurrent.TimeoutException;

import ru.labirint.android.interactor.location.error.LocationSecurityException;
import ru.labirint.android.interactor.location.error.LocationServiceUnavailableException;
import rx.Observable;

/**
 * Интерфей сервиса, поставляющего локацию пользователя
 */
public interface LocationService {

    /**
     * @return observable, который эмитит локацию пользователя один раз
     * может также эмитит ошибки: {@link LocationServiceUnavailableException},
     * {@link TimeoutException}, {@link LocationSecurityException}
     * <p>
     * Если ранее был запрос на локацию и он не завершился, то старый запрос переиспользуется
     */
    Observable<LocationData> getLocation(long timeout);

    /**
     * запускает запрос на получение локации в фоне
     * @param timeoutMs
     */
    void startGetLocationInBackground(long timeoutMs);
}
