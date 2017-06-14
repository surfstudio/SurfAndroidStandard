package com.surf.myapplication.interactor.location;

import com.agna.ferro.mvp.component.scope.PerApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {

    @Provides
    @PerApplication
    public LocationProvider provideLocationProvider(LocationProviderImpl locationProvider) {
        return locationProvider;
    }

    @Provides
    @PerApplication
    public LocationService provideLocationService(LocationServiceImpl locationService) {
        return locationService;
    }

}
