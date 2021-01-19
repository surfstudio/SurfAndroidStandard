package ru.surfstudio.android.biometrics.error

/**
 * The user or system canceled the operation.
 * For example, this may happen when the user is switched, the device is locked or another pending operation prevents
 * or disables or simply user canceled operation by closing the dialog.
 */
class CanceledException(message: CharSequence?) : BiometricsException(message)