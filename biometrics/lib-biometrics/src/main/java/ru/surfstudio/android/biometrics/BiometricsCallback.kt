/*
  Copyright (c) 2020-present, SurfStudio LLC, Akhbor Akhrorov.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.biometrics

import android.annotation.SuppressLint
import android.content.Context
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.surfstudio.android.biometrics.error.*

/**
 * Wrapper class for biometric scanner
 *
 * @param context applicationContext
 */
internal class BiometricsCallback(
    private val context: Context
) : BiometricPrompt.AuthenticationCallback() {

    private var biometricPrompt: BiometricPrompt? = null
    private var selfCancelled: Boolean = false
    private val biometricsManager: BiometricManager = BiometricManager.from(context)
    private var biometricsListener: BiometricsListener? = null

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        if (!selfCancelled) {
            val fingerprintException: Exception = when (errorCode) {
                BiometricConstants.ERROR_HW_UNAVAILABLE -> HardwareUnavailableException(errString)
                BiometricConstants.ERROR_UNABLE_TO_PROCESS,
                BiometricConstants.ERROR_NO_SPACE -> NoSpaceLeftException(errString)
                BiometricConstants.ERROR_TIMEOUT -> TimeoutException(errString)
                BiometricConstants.ERROR_USER_CANCELED,
                BiometricConstants.ERROR_CANCELED -> CanceledException(errString)
                BiometricConstants.ERROR_LOCKOUT -> LockedOutException(errString)
                BiometricConstants.ERROR_VENDOR -> VendorErrorException(errString)
                BiometricConstants.ERROR_LOCKOUT_PERMANENT -> LockoutPermanentException(errString)
                BiometricConstants.ERROR_NO_BIOMETRICS -> NoBiometricsRegisteredException(errString)
                BiometricConstants.ERROR_HW_NOT_PRESENT -> HardwareNotFoundException(errString)
                BiometricConstants.ERROR_NEGATIVE_BUTTON -> NegativeButtonException(errString)
                BiometricConstants.ERROR_NO_DEVICE_CREDENTIAL -> NoDeviceCredential(errString)
                else -> BiometricsException(errString)
            }
            biometricsListener?.onError(fingerprintException)
        }
    }

    override fun onAuthenticationFailed() {
        /* do nothing */
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        biometricsListener?.onSuccess()
    }

    /**
     * Initializing biometrics dialog
     */
    @SuppressLint("MissingPermission")
    fun <T> initFingerprintListener(
        biometricsListener: BiometricsListener,
        screen: T
    ) {
        this.biometricsListener = biometricsListener

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.biometric_dialog_title))
            .setSubtitle(context.getString(R.string.biometric_dialog_subtitle))
            .setNegativeButtonText(context.getString(R.string.biometric_dialog_cancel))
            .build()

        biometricPrompt = when (screen) {
            is FragmentActivity -> BiometricPrompt(screen, { it.run() }, this)
            is Fragment -> BiometricPrompt(screen, { it.run() }, this)
            else -> throw IllegalArgumentException("Screen type must be FragmentActivity or Fragment")
        }

        selfCancelled = false
        biometricPrompt?.authenticate(info)
    }

    /**
     * Stop listening for scanner
     */
    fun stopListening() {
        selfCancelled = true
        biometricPrompt?.cancelAuthentication()
        biometricPrompt = null
    }

    /**
     * Check for biometrics scanner
     */
    fun hasBiometricsScanner() =
        biometricsManager.canAuthenticate() !in arrayOf(
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
        )

    /**
     * Check for registered biometric data in device
     */
    fun hasBiometricsRegistered() =
        biometricsManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
}
