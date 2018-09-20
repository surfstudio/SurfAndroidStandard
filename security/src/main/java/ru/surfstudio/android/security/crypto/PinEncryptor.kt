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
import javax.crypto.spec.PBEKeySpec

/**
 * Класс для шифрования данных с помощью pin-кода
 */
class PinEncryptor(pin: String): SignEncryptor<String>(pin) {

    override fun getEncryptCipher(salt: ByteArray): Cipher {
        return SecurityUtils.getEncryptCipher(getSpec(salt))
    }

    override fun getDecryptCipher(salt: ByteArray, iv: ByteArray): Cipher {
        return SecurityUtils.getDecryptCipher(getSpec(salt), iv)
    }

    private fun getSpec(salt: ByteArray): PBEKeySpec = SecurityUtils.getSpec(sign, salt)
}