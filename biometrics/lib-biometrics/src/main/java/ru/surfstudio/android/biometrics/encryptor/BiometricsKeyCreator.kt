package ru.surfstudio.android.biometrics.encryptor

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.Key
import java.security.KeyStore
import javax.crypto.KeyGenerator

/**
 * Class for creating secured key for biometrics.
 *
 * @param shouldInvalidatedByEnrollment нужно ли инвалидировать при изменении состава биометрии
 * @param shouldInvalidatedByEnrollment if true, saved key will be invalidated on new biometrics enrollment
 * @param keyAlgorithm name of the encrypt algorithm
 */
class BiometricsKeyCreator(
    private val shouldInvalidatedByEnrollment: Boolean = true,
    private val keyAlgorithm: String = KEY_ALGORITHM_AES
) {

    fun getSecretKey(shouldCreate: Boolean): Key {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return if (shouldCreate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                createSecretKey(keyStore)
            else throw IllegalAccessException("Method only work on api >= 23")
        } else {
            keyStore.getKey(ALIAS_BIOMETRICS, null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createSecretKey(keystore: KeyStore): Key {
        val builder = KeyGenParameterSpec.Builder(
            ALIAS_BIOMETRICS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationValidityDurationSeconds(5)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(shouldInvalidatedByEnrollment)
            builder.setUserAuthenticationValidWhileOnBody(true)
        }

        val keyGenerator =
            KeyGenerator.getInstance(keyAlgorithm, keystore.provider)
        keyGenerator
            .init(builder.build())
        return keyGenerator.generateKey()
    }

    private companion object {
        const val ALIAS_BIOMETRICS = "BMTRCS"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALGORITHM_AES = "AES"
    }
}