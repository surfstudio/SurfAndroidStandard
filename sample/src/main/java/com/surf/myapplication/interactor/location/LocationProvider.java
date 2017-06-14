package com.surf.myapplication.interactor.location;

import android.support.annotation.WorkerThread;

/**
 * обертка над библиотекой, предоставляющей доступ к локации пользователя
 */
public interface LocationProvider {
    void reset();

    @WorkerThread
    void getLocation(LocationListener locationListener);

    interface LocationListener {
        void onSuccess(LocationData locationData);
        void onError(Throwable e);
    }

}
