package ru.surfstudio.android.biometrics.error

/**
 * Base class for biometrics exceptions
 */
open class BiometricsException(private val messageFingerprint: CharSequence? = null) : Exception() {

    override val message: String?
        get() = messageFingerprint?.toString()
}