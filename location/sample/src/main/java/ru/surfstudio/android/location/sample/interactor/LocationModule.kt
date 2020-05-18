package ru.surfstudio.android.location.sample.interactor

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.location.ILocationService
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.sample.ui.screen.common.CommonLocationPermissionRequest

@Module
class LocationModule {

    @Provides
    internal fun provideLocationService(context: Context): ILocationService = LocationService(context)

    @Provides
    internal fun provideCommonLocationPermissionRequest(
            context: Context
    ): CommonLocationPermissionRequest {
        return CommonLocationPermissionRequest(context)
    }
}