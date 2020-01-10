package ru.surfstudio.android.location.sample.ui.screen.common

import android.content.Context
import ru.surfstudio.android.core.ui.permission.screens.settings_rational.SettingsRationalDialogParamsBuilder
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.LocationPermissionRequest
import ru.surfstudio.android.location_sample.R

/**
 * Запрос разрешения на доступ к местоположению.
 */
class CommonLocationPermissionRequest(context: Context) : LocationPermissionRequest() {

    init {
        showPermissionsRational = true
        permissionsRationalStr = context.getString(R.string.permission_rational)

        showSettingsRational = true
        settingsRationalDialogParams = SettingsRationalDialogParamsBuilder()
            .rationalTxt(context.getString(R.string.settings_rational))
            .build()
    }
}