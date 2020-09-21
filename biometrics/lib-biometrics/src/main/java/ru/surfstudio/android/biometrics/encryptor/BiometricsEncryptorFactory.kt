package ru.surfstudio.android.biometrics.encryptor

import ru.surfstudio.android.security.crypto.KeyEncryptor
import java.security.Key

/**
 * Фабрика класса шифрования.
 * Для быстрой инициализации можно использовать [DefaultBiometricsEncryptor]
 */
interface BiometricsEncryptorFactory {
    fun getEncyptor(key: Key): KeyEncryptor<Key>
}