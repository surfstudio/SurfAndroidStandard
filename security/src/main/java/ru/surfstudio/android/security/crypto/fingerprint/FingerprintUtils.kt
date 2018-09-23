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
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.security.crypto.security.SecurityUtils
import ru.surfstudio.android.security.crypto.security.initEncryptMode
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object FingerprintUtils {

    internal const val DEFAULT_ALIAS_FINGERPRINT = "FNGRPRNT"

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    @TargetApi(Build.VERSION_CODES.M)
    fun getFingerprintCryptoObject(
            fingerprintAlias: String = DEFAULT_ALIAS_FINGERPRINT,
            cipherTransformation: String = SecurityUtils.DEFAULT_CIPHER_TRANSFORMATION
    ): FingerprintManager.CryptoObject? {
        return getFingerprintCryptoObject(
                cipherTransformation,
                getSecretKeyForFingerprint(fingerprintAlias))
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun createFingerprintCryptoObject(
            fingerprintAlias: String = DEFAULT_ALIAS_FINGERPRINT,
            cipherTransformation: String = SecurityUtils.DEFAULT_CIPHER_TRANSFORMATION
    ): FingerprintManager.CryptoObject? {
        return getFingerprintCryptoObject(
                cipherTransformation,
                createFingerprintKey(fingerprintAlias))
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getFingerprintCryptoObject(
            cipherTransformation: String,
            secretKey: SecretKey?
    ): FingerprintManager.CryptoObject? = try {
        FingerprintManager.CryptoObject(
                SecurityUtils
                        .getCipherInstance(cipherTransformation)
                        .initEncryptMode(secretKey)
        )
    } catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }

    internal fun getSecretKeyForFingerprint(alias: String = DEFAULT_ALIAS_FINGERPRINT): SecretKey {
        return getAndroidKeyStore().getKey(alias, null) as SecretKey
    }

    private fun getAndroidKeyStore(): KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun createFingerprintKey(alias: String = DEFAULT_ALIAS_FINGERPRINT,
                                     keystore: KeyStore = getAndroidKeyStore()): SecretKey? = try {
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(false)
                .setUserAuthenticationValidityDurationSeconds(-1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(false)
            builder.setUserAuthenticationValidWhileOnBody(false)
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, keystore.provider)
        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
    } catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }
}