package ru.surfstudio.android.biometrics.encryptor

import ru.surfstudio.android.security.crypto.KeyEncryptor
import java.security.Key

/**
 * Encryptor class Factory
 * For fast initialization you can use [DefaultBiometricsEncryptor]
 */
interface BiometricsEncryptorFactory {
    fun getEncyptor(key: Key): KeyEncryptor<Key>
}