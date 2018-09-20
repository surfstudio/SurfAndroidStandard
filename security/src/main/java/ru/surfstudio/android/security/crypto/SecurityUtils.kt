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
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import ru.surfstudio.android.logger.Logger
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec

object SecurityUtils {

    private const val ALIAS_FINGERPRINT = "FNGRPRNT"

    private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding"
    private const val KEY_ALGORITHM = "PBKDF2WithHmacSHA1"

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    private const val KEY_LENGTH = 256
    private const val ITERATION_COUNT = 16384
    private const val DEFAULT_SALT_SIZE = 16

    fun generateSalt(saltSize: Int = DEFAULT_SALT_SIZE): ByteArray {
        val salt = ByteArray(saltSize)
        SecureRandom().nextBytes(salt)
        return salt
    }

    fun getEncryptCipher(spec: PBEKeySpec): Cipher {
        return Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(
                    Cipher.ENCRYPT_MODE,
                    SecretKeyFactory
                            .getInstance(KEY_ALGORITHM)
                            .generateSecret(spec))
        }
    }

    fun getDecryptCipher(spec: PBEKeySpec, iv: ByteArray): Cipher {
        return Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(
                    Cipher.DECRYPT_MODE,
                    SecretKeyFactory
                            .getInstance(KEY_ALGORITHM)
                            .generateSecret(spec),
                    IvParameterSpec(iv))
        }
    }

    fun getSpec(pin: String, salt: ByteArray): PBEKeySpec {
        return PBEKeySpec(pin.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
    }

    fun getSecretKeyForFingerPrint(): SecretKey {
        return getAndroidKeyStore().getKey(ALIAS_FINGERPRINT, null) as SecretKey
    }

    fun getAndroidKeyStore(): KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun getFingerPrintCryptoObject(secretKey: SecretKey?): FingerprintManager.CryptoObject? = try {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        FingerprintManager.CryptoObject(cipher)
    } catch (throwable: Throwable) {
        Logger.e(throwable)
        null
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun createFingerprintKey(alias: String = ALIAS_FINGERPRINT,
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