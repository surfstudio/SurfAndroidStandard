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

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.security.crypto.SecurityUtils.getSecretKeyForFingerPrint
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Класс для шифрования данных с помощью отпечатка пальца
 */
class FingerprintEncryptor : Encryptor<FingerprintManagerCompat.CryptoObject, SecretValue> {

    override fun encrypt(secureData: String,
                         sign: FingerprintManagerCompat.CryptoObject): SecretValue? = try {
        val salt = SecurityUtils.generateSalt()
        val cipher = sign.cipher
        if (cipher != null) {
            SecretValue(cipher.doFinal(secureData.toByteArray()), cipher.iv, salt)
        } else {
            null
        }
    } catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }

    override fun decrypt(sign: FingerprintManagerCompat.CryptoObject,
                         encrypted: SecretValue): String? = try {
        val cipher = sign.cipher
        if (cipher != null) {
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeyForFingerPrint(), IvParameterSpec(encrypted.iv))
            String(cipher.doFinal(encrypted.secret))
        } else {
            null
        }
    } catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }
}