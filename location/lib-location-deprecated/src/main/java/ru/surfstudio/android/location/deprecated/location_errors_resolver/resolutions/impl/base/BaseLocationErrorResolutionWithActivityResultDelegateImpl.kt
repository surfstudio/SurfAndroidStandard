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
package ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.base

import android.app.Activity
import android.content.Intent
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import ru.surfstudio.android.core.ui.ScreenType
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.result.ActivityResultDelegate
import ru.surfstudio.android.location.deprecated.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.deprecated.exceptions.UserDeniedException

/**
 * Основа для решения проблем получения местоположения с использованием [ActivityResultDelegate].
 */
@Deprecated("Prefer using new implementation")
abstract class BaseLocationErrorResolutionWithActivityResultDelegateImpl<E : Throwable>(
        screenEventDelegateManager: ScreenEventDelegateManager
) : BaseLocationErrorResolutionImpl<E>(), ActivityResultDelegate {

    protected abstract val requestCode: Int

    private var resolvingThrowable: E? = null
    private var completableEmitter: CompletableEmitter? = null

    init {
        screenEventDelegateManager.registerDelegate(this, ScreenType.ACTIVITY)
    }

    protected abstract fun performResolutionRequest(resolvingThrowable: E)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (this.requestCode != requestCode) {
            return false
        }

        val isResolved = resultCode == Activity.RESULT_OK
        handleResolutionResult(isResolved)

        return true
    }

    final override fun performWithCastedThrowable(resolvingThrowable: E): Completable =
            Completable.create { completableEmitter ->
                setArgs(resolvingThrowable, completableEmitter)
                try {
                    performResolutionRequest(resolvingThrowable)
                } catch (t: Throwable) {
                    completableEmitter.onError(ResolutionFailedException(resolvingThrowable, t))
                    clearArgs()
                }
            }

    private fun handleResolutionResult(isResolved: Boolean) {
        val nonNullCompletableEmitter = completableEmitter ?: return
        if (nonNullCompletableEmitter.isDisposed) return

        if (isResolved) {
            nonNullCompletableEmitter.onComplete()
        } else {
            nonNullCompletableEmitter.onError(ResolutionFailedException(resolvingThrowable, UserDeniedException()))
        }
        clearArgs()
    }

    private fun setArgs(
            resolvingException: E,
            completableEmitter: CompletableEmitter
    ) {
        this.resolvingThrowable = resolvingException
        this.completableEmitter = completableEmitter
    }

    private fun clearArgs() {
        resolvingThrowable = null
        completableEmitter = null
    }
}