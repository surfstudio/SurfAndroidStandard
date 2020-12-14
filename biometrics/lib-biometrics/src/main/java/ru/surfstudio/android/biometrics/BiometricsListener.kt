package ru.surfstudio.android.biometrics

/**
 * Listener for result of the biometrics scanner
 */
internal interface BiometricsListener {

    fun onSuccess()

    fun onError(biometricsException: Exception)
}