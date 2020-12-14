package ru.surfstudio.android.biometrics.encryptor

import ru.surfstudio.android.security.crypto.KeyEncryptor
import ru.surfstudio.android.security.crypto.security.SecurityUtils
import ru.surfstudio.android.security.crypto.security.initDecryptMode
import ru.surfstudio.android.security.crypto.security.initEncryptMode
import java.security.Key
import javax.crypto.Cipher

/**
 * Cipher with biometric key.
 *
 * @param key key from [BiometricsKeyCreator]
 * @param cipherTransformation transformation algorithm
 */
class DefaultBiometricsEncryptor(
    key: Key,
    private val cipherTransformation: String = SecurityUtils.DEFAULT_CIPHER_TRANSFORMATION
) : KeyEncryptor<Key>(key) {

    override fun getDecryptCipher(sign: Key, salt: ByteArray, iv: ByteArray): Cipher {
        return getCipher().initDecryptMode(sign, iv)
    }

    override fun getEncryptCipher(sign: Key, salt: ByteArray): Cipher {
        return getCipher().initEncryptMode(sign)
    }

    private fun getCipher(): Cipher = Cipher.getInstance(cipherTransformation)
}