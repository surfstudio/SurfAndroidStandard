package com.surf.myapplication.interactor.initialization;

import com.agna.ferro.mvp.component.scope.PerApplication;

import javax.inject.Inject;

import ru.labirint.android.app.log.Logger;
import ru.labirint.android.domain.user.Region;
import ru.labirint.android.interactor.auth.AuthService;
import ru.labirint.android.interactor.initialization.migration.AppMigrationManager;
import ru.labirint.android.interactor.location.LocationData;
import ru.labirint.android.interactor.location.LocationService;
import ru.labirint.android.interactor.region.RegionRepository;
import rx.Observable;

/**
 * Инициализирует приложение
 */
@PerApplication
public class InitializeAppInteractorImpl implements InitializeAppInteractor {

    public static final int GETTING_LOCATION_TIMEOUT = 4000; //ms

    private final LocationService locationService;
    private final AppMigrationManager appMigrationManager;
    private final AuthService authService;
    private final RegionRepository regionRepository;

    @Inject
    public InitializeAppInteractorImpl(LocationService locationService,
                                       AppMigrationManager appMigrationManager,
                                       AuthService authService,
                                       RegionRepository regionRepository) {
        this.locationService = locationService;
        this.appMigrationManager = appMigrationManager;
        this.authService = authService;
        this.regionRepository = regionRepository;
    }

    /**
     * инициализирует приложение
     *
     * @return observable, который всегда завершается успешно
     */
    @Override
    public Observable<Object> initialize() {
        return appMigrationManager.tryMigrateApp()
                .flatMap(o -> authService.updateToken())
                .flatMap(o -> regionRepository.getRegion())
                .flatMap(region -> tryUpdateRegion(region))
                .map(o -> null)
                .doOnError(e-> Logger.e(e, "err"))
                .onErrorReturn(e -> null);
    }

    private Observable<Void> tryUpdateRegion(Region currentRegion) {
        Observable<Void> resultObservable;
        if (currentRegion == null) {
            resultObservable = locationService.getLocation(GETTING_LOCATION_TIMEOUT)
                    .onErrorReturn(e -> LocationData.empty())
                    .flatMap(location -> regionRepository.updateRegion(location.getLat(), location.getLon()))
                    .onErrorReturn(e -> null)
                    .map(newRegion -> null);
        } else {
            resultObservable = Observable.just(null);
        }
        return resultObservable;
    }

}
