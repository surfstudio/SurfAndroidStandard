package ru.surfstudio.android.biometrics.error

/**
 * Error state returned when the current request has been running too long. This is intended to
 * prevent programs from waiting for the biometric sensor indefinitely. The timeout is platform
 * and sensor-specific, but is generally on the order of 30 seconds.
 */
class TimeoutException(message: CharSequence?) : BiometricsException(message)