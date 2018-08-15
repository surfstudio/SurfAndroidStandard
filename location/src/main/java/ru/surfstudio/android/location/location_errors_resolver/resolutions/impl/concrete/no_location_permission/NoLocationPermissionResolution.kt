/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission

import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.location.domain.LocationAccuracy
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.ResolvingFailedException
import ru.surfstudio.android.location.exceptions.UserDeniedException
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.base.BaseLocationErrorResolutionImpl

/**
 * Решение проблемы [NoLocationPermissionException].
 *
 * @param locationAccuracy запрашиваемая точность определения метоположения.
 * @param permissionManager менеджер разрешений.
 */
class NoLocationPermissionResolution(
        locationAccuracy: LocationAccuracy,
        private val permissionManager: PermissionManager
) : BaseLocationErrorResolutionImpl<NoLocationPermissionException>() {

    override val resolvingExceptionClass = NoLocationPermissionException::class.java

    private val locationPermissionRequest = LocationPermissionRequest(locationAccuracy)

    override fun performWithCastedException(
            resolvingException: NoLocationPermissionException,
            onSuccessAction: () -> Unit,
            onFailureAction: (ResolvingFailedException) -> Unit
    ) {
        permissionManager.request(locationPermissionRequest)
                .subscribe(
                        { isSuccessful ->
                            if (isSuccessful) {
                                onSuccessAction()
                            } else {
                                onFailureAction(ResolvingFailedException(resolvingException, UserDeniedException()))
                            }
                        },
                        { throwable -> onFailureAction(ResolvingFailedException(resolvingException, throwable)) }
                )
    }
}