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
package ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.base

import android.app.Activity
import android.content.Intent
import ru.surfstudio.android.core.ui.event.result.ActivityResultDelegate
import ru.surfstudio.android.location.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.exceptions.UserDeniedException

/**
 * Основа для решения проблем получения метоположения с использованием [ActivityResultDelegate].
 */
abstract class BaseLocationErrorResolutionWithActivityResultDelegateImpl<E : Exception> :
        BaseLocationErrorResolutionImpl<E>() {

    protected abstract val requestCode: Int

    private var resolvingException: E? = null
    private var onSuccessAction: (() -> Unit)? = null
    private var onFailureAction: ((ResolutionFailedException) -> Unit)? = null

    protected abstract fun performResolutionRequest(resolvingException: E)

    final override fun performWithCastedException(
            resolvingException: E,
            onSuccessAction: () -> Unit,
            onFailureAction: (ResolutionFailedException) -> Unit
    ) {
        setArgs(resolvingException, onSuccessAction, onFailureAction)

        try {
            performResolutionRequest(resolvingException)
        } catch (e: Exception) {
            onFailureAction(ResolutionFailedException(resolvingException, e))
            clearArgs()
        }
    }

    private fun setArgs(
            resolvingException: E,
            onSuccessAction: () -> Unit,
            onFailureAction: (ResolutionFailedException) -> Unit
    ) {
        this.resolvingException = resolvingException
        this.onSuccessAction = onSuccessAction
        this.onFailureAction = onFailureAction
    }

    private fun clearArgs() {
        resolvingException = null
        onSuccessAction = null
        onFailureAction = null
    }

    private fun onResolutionResult(resolved: Boolean) {
        if (resolved) {
            onSuccessAction?.invoke()
        } else {
            onFailureAction?.invoke(ResolutionFailedException(resolvingException, UserDeniedException()))
        }
        clearArgs()
    }

    inner class ResolutionActivityResultDelegate : ActivityResultDelegate {

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
            if (this@BaseLocationErrorResolutionWithActivityResultDelegateImpl.requestCode != requestCode) {
                return false
            }

            val resolved = resultCode == Activity.RESULT_OK
            onResolutionResult(resolved)
            return true
        }
    }
}