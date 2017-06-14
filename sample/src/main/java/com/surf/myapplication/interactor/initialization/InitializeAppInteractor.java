package com.surf.myapplication.interactor.initialization;

import rx.Observable;

public interface InitializeAppInteractor {
    Observable<Object> initialize();
}
