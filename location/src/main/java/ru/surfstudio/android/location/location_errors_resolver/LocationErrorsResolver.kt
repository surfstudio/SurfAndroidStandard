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
package ru.surfstudio.android.location.location_errors_resolver

import ru.surfstudio.android.location.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution

/**
 * Утилита для решения проблем, связанных с невозможностью получения местоположения.
 */
internal object LocationErrorsResolver {

    /**
     * Решить проблемы, связанные с невозможностью получения местоположения.
     *
     * @param resolvingExceptions [List], содержащий исключения связанные с невозможностью получения местоположения.
     * @param resolutions [List], содержащий решения проблем связанных с невозможностью получения местоположения.
     * @param onFinishAction метод, вызываемый при завершении решения проблем.
     * @param onFailureAction метод вызываемый в случае, если попытка решения проблем не удалась.
     */
    fun resolve(
            resolvingExceptions: List<Exception>,
            resolutions: List<LocationErrorResolution<*>>,
            onFinishAction: (unresolvedExceptions: List<Exception>) -> Unit,
            onFailureAction: (ResolutionFailedException) -> Unit
    ) {
        if (resolvingExceptions.isEmpty() || resolutions.isEmpty()) {
            onFinishAction(resolvingExceptions)
            return
        }

        for (resolvingException in resolvingExceptions) {
            for (resolution in resolutions) {
                if (!resolution.resolvingExceptionClass.isInstance(resolvingException)) {
                    continue
                }

                resolution.perform(
                        resolvingException,
                        onSuccessAction = {
                            val exceptionsWithoutResolvedException = resolvingExceptions.toMutableList()
                            val resolutionsWithoutPerformedResolution = resolutions.toMutableList()

                            exceptionsWithoutResolvedException.remove(resolvingException)
                            resolutionsWithoutPerformedResolution.remove(resolution)

                            resolve(
                                    exceptionsWithoutResolvedException,
                                    resolutionsWithoutPerformedResolution,
                                    onFinishAction,
                                    onFailureAction
                            )
                        },
                        onFailureAction = onFailureAction
                )
            }
        }
    }
}