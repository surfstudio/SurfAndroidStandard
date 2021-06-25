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
package ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception

import com.google.android.gms.common.api.ResolvableApiException
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.base.BaseLocationErrorResolutionWithActivityResultDelegateImpl

/**
 * Решение проблемы [ResolvableApiException].
 *
 * @param activityProvider Провайдер активити.
 */
class ResolvableApiExceptionResolution(
        screenEventDelegateManager: ScreenEventDelegateManager,
        private val activityProvider: ActivityProvider
) : BaseLocationErrorResolutionWithActivityResultDelegateImpl<ResolvableApiException>(screenEventDelegateManager) {

    override val resolvingThrowableClass = ResolvableApiException::class.java
    override val requestCode = 1002

    override fun performResolutionRequest(resolvingThrowable: ResolvableApiException) {
        resolvingThrowable.startResolutionForResult(activityProvider.get(), requestCode)
    }
}