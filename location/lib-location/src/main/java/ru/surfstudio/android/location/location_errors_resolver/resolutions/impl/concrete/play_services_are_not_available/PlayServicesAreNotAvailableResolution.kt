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
package ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GoogleApiAvailability
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.Provider
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException
import ru.surfstudio.android.location.exceptions.PlayServicesAvailabilityErrorIsNotResolvableException
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.base.BaseLocationErrorResolutionWithActivityResultDelegateImpl

/**
 * Решение проблемы [PlayServicesAreNotAvailableException].
 *
 * @param activityProvider Провайдер активити.
 */
class PlayServicesAreNotAvailableResolution(
        screenEventDelegateManager: ScreenEventDelegateManager,
        private val activityProvider: ActivityProvider
) : BaseLocationErrorResolutionWithActivityResultDelegateImpl<PlayServicesAreNotAvailableException>(
        screenEventDelegateManager
) {

    override val resolvingThrowableClass = PlayServicesAreNotAvailableException::class.java
    override val requestCode = 1001

    override fun performResolutionRequest(resolvingThrowable: PlayServicesAreNotAvailableException) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()

        if (!googleApiAvailability.isUserResolvableError(resolvingThrowable.connectionResult)) {
            throw PlayServicesAvailabilityErrorIsNotResolvableException()
        }

        googleApiAvailability
                .getErrorDialog(activityProvider.get(), resolvingThrowable.connectionResult, requestCode)
                .show()
    }
}