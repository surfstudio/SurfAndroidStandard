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

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import ru.surfstudio.android.security.crypto.SecurityUtils.getSecretKeyForFingerPrint
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Класс для шифрования данных с помощью отпечатка пальца
 */
class FingerprintEncryptor(cryptoObject: FingerprintManager.CryptoObject
): SignEncryptor<FingerprintManager.CryptoObject>(cryptoObject) {

    @TargetApi(Build.VERSION_CODES.M)
    override fun getEncryptCipher(salt: ByteArray): Cipher = sign.cipher

    @TargetApi(Build.VERSION_CODES.M)
    override fun getDecryptCipher(salt: ByteArray, iv: ByteArray): Cipher {
        return sign.cipher.apply {
            init(Cipher.DECRYPT_MODE, getSecretKeyForFingerPrint(), IvParameterSpec(iv))
        }
    }
}