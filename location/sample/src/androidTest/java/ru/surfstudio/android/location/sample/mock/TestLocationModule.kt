package ru.surfstudio.android.location.sample.mock

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.location.ILocationService
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.sample.ui.screen.common.CommonLocationPermissionRequest

@Module
class TestLocationModule {

    @Provides
    internal fun provideLocationService(context: Context): ILocationService {
        return LocationService(
                context,
                LocationServices.getFusedLocationProviderClient(context).apply {
                    setMockMode(true)
                    setMockLocation(
                            Location("").apply {
                                latitude = 1.2345
                                longitude = 1.2345
                            }
                    )
                }
        )
    }

    @Provides
    internal fun provideCommonLocationPermissionRequest(
            context: Context
    ): CommonLocationPermissionRequest {
        return CommonLocationPermissionRequest(context)
    }
}