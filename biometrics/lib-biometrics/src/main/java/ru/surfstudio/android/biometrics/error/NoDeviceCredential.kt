package ru.surfstudio.android.biometrics.error

/**
 * The device does not have pin, pattern, or password set up.
 */
class NoDeviceCredential(message: CharSequence?) : BiometricsException(message)