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
package ru.surfstudio.android.security.crypto.fingerprint

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import ru.surfstudio.android.security.crypto.fingerprint.FingerprintUtils.DEFAULT_ALIAS_FINGERPRINT
import ru.surfstudio.android.security.crypto.fingerprint.FingerprintUtils.getSecretKeyForFingerPrint
import ru.surfstudio.android.security.crypto.SignEncryptor
import ru.surfstudio.android.security.crypto.security.initDecryptMode
import javax.crypto.Cipher

/**
 * Класс для шифрования данных с помощью отпечатка пальца
 */
class FingerprintEncryptor(
        cryptoObject: FingerprintManager.CryptoObject,
        private val fingerprintAlias: String = DEFAULT_ALIAS_FINGERPRINT
): SignEncryptor<FingerprintManager.CryptoObject>(cryptoObject) {

    @TargetApi(Build.VERSION_CODES.M)
    override fun getEncryptCipher(sign: FingerprintManager.CryptoObject,
                                  salt: ByteArray): Cipher {
        return sign.cipher
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun getDecryptCipher(sign: FingerprintManager.CryptoObject,
                                  salt: ByteArray,
                                  iv: ByteArray): Cipher {
        return sign.cipher.initDecryptMode(getSecretKeyForFingerPrint(fingerprintAlias), iv)
    }
}