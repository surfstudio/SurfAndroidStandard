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
class PinEncryptor(private val pin: String,
                   private val cipherTransformation: String = SecurityUtils.DEFAULT_CIPHER_TRANSFORMATION,
                   private val keyAlgorithm: String = SecurityUtils.DEFAULT_KEY_ALGORITHM
): CipherEncryptor() {

    companion object {
        private const val KEY_LENGTH = 256
        private const val ITERATION_COUNT = 16384
    }

    override fun getEncryptCipher(salt: ByteArray): Cipher {
        return Cipher.getInstance(cipherTransformation).apply {
            init(Cipher.ENCRYPT_MODE, generateSecretKey(salt))
        }
    }

    override fun getDecryptCipher(salt: ByteArray, iv: ByteArray): Cipher {
        return Cipher.getInstance(cipherTransformation).apply {
            init(Cipher.DECRYPT_MODE, generateSecretKey(salt), IvParameterSpec(iv))
        }
    }

    private fun generateSecretKey(salt: ByteArray): SecretKey {
        return SecretKeyFactory
                .getInstance(keyAlgorithm)
                .generateSecret(getSpec(salt))
    }

    private fun getSpec(salt: ByteArray): PBEKeySpec {
        return PBEKeySpec(pin.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
    }
}