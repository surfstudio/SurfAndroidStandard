package ru.surfstudio.android.biometrics.error

/**
 * The device does not have a biometric sensor.
 */
class HardwareNotFoundException(message: CharSequence?) : BiometricsException(message)