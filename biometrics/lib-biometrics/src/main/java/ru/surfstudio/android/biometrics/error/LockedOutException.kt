package ru.surfstudio.android.biometrics.error

/**
 * The operation was canceled because the API is locked out due to too many attempts.
 * This occurs after 5 failed attempts, and lasts for 30 seconds.
 */
class LockedOutException(message: CharSequence?) : BiometricsException(message)