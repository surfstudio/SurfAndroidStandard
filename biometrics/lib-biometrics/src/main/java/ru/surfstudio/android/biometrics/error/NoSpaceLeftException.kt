package ru.surfstudio.android.biometrics.error

/**
 * Error state returned for operations like enrollment; the operation cannot be completed
 * because there's not enough storage remaining to complete the operation.
 */
class NoSpaceLeftException(message: CharSequence?) : BiometricsException(message)