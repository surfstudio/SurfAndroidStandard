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

import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.security.crypto.SecurityUtils.getDecryptCipher
import ru.surfstudio.android.security.crypto.SecurityUtils.getEncryptCipher
import ru.surfstudio.android.security.crypto.SecurityUtils.getSpec

/**
 * Класс для шифрования данных с помощью pin-кода
 */
class PinEncryptor : Encryptor<String, SecretValue> {

    override fun encrypt(secureData: String, sign: String): SecretValue? = try {
        val salt = SecurityUtils.generateSalt()
        val spec = getSpec(sign, salt)
        val cipher = getEncryptCipher(spec)

        SecretValue(cipher.doFinal(secureData.toByteArray()), cipher.iv, salt)
    }  catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }

    override fun decrypt(sign: String, encrypted: SecretValue): String? = try {
        val spec = getSpec(sign, encrypted.salt)
        val cipher = getDecryptCipher(spec, encrypted.iv)

        String(cipher.doFinal(encrypted.secret))
    } catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }
}