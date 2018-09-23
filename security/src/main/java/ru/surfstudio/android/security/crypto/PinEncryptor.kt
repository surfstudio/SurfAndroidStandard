/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.security.crypto

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec

/**
 * Класс для шифрования данных с помощью pin-кода
 */
class PinEncryptor(
        pin: String,
        private val cipherTransformation: String = SecurityUtils.DEFAULT_CIPHER_TRANSFORMATION,
        private val keyAlgorithm: String = SecurityUtils.DEFAULT_KEY_ALGORITHM
): SignEncryptor<String>(pin) {

    companion object {
        private const val KEY_LENGTH = 256
        private const val ITERATION_COUNT = 16384
    }

    override fun getEncryptCipher(sign: String, salt: ByteArray): Cipher {
        return Cipher.getInstance(cipherTransformation).apply {
            init(Cipher.ENCRYPT_MODE, generateSecretKey(sign, salt))
        }
    }

    override fun getDecryptCipher(sign: String, salt: ByteArray, iv: ByteArray): Cipher {
        return Cipher.getInstance(cipherTransformation).apply {
            init(Cipher.DECRYPT_MODE, generateSecretKey(sign, salt), IvParameterSpec(iv))
        }
    }

    private fun generateSecretKey(sign: String, salt: ByteArray): SecretKey {
        return SecretKeyFactory
                .getInstance(keyAlgorithm)
                .generateSecret(getSpec(sign, salt))
    }

    private fun getSpec(sign: String, salt: ByteArray): PBEKeySpec {
        return PBEKeySpec(sign.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
    }
}