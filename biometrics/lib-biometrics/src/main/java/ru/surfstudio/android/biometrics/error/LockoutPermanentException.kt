package ru.surfstudio.android.biometrics.error

/**
 * The operation was canceled because ERROR_LOCKOUT occurred too many times.
 * Biometric authentication is disabled until the user unlocks with strong authentication
 * (PIN/Pattern/Password)
 */
class LockoutPermanentException(message: CharSequence?) : BiometricsException(message)