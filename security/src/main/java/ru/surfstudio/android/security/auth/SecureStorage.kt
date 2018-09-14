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
package ru.surfstudio.android.security.auth

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import ru.surfstudio.android.shared.pref.SettingsUtil
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec

/**
 * Защищенное хранилище, использующее AES-шифрование.
 */
class SecureStorage(private val noBackupSharedPref: SharedPreferences) {

    companion object {
        private const val KEY_SECURE_DATA_BY_PIN = "data_pin"
        private const val KEY_SECURE_DATA_BY_FINGERPRINT = "data_fngprnt"

        private const val ALIAS_FINGERPRINT = "FNGRPRNT"

        private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding"
        private const val KEY_ALGORITHM = "PBKDF2WithHmacSHA1"

        private const val ANDROID_KEYSTORE = "AndroidKeyStore"

        private const val SALT_SIZE = 16
        private const val KEY_LENGTH = 256
        private const val ITERATION_COUNT = 16384
    }

    fun saveSecureData(secureData: String,
                       pin: String,
                       cryptoObject: FingerprintManager.CryptoObject?): Boolean = try {
        val salt = generateSalt()
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance(KEY_ALGORITHM).generateSecret(spec))

        val secretValue = SecretValue(cipher.doFinal(secureData.toByteArray()), cipher.iv, salt)
        SettingsUtil.putString(noBackupSharedPref, KEY_SECURE_DATA_BY_PIN, secretValue.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cryptoObject != null) {
            SettingsUtil.putString(
                    noBackupSharedPref,
                    KEY_SECURE_DATA_BY_FINGERPRINT,
                    SecretValue(
                            cryptoObject.cipher.doFinal(secureData.toByteArray()),
                            cryptoObject.cipher.iv,
                            generateSalt())
                            .toString())
        }

        true
    } catch (ignored: Throwable) {
        false
    }

    fun getSecureData(pin: String): String? = try {
        val secretValue = SecretValue.fromString(SettingsUtil.getString(noBackupSharedPref, KEY_SECURE_DATA_BY_PIN))
        val spec = PBEKeySpec(pin.toCharArray(), secretValue.salt, ITERATION_COUNT, KEY_LENGTH)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeyFactory.getInstance(KEY_ALGORITHM).generateSecret(spec),
                IvParameterSpec(secretValue.iv))

        String(cipher.doFinal(secretValue.secret))
    } catch (ignored: Throwable) {
        null
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun getSecureData(cryptoObject: FingerprintManager.CryptoObject): String? = try {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        val key = keyStore.getKey(ALIAS_FINGERPRINT, null) as SecretKey
        val secretValue = SecretValue.fromString(SettingsUtil.getString(noBackupSharedPref, KEY_SECURE_DATA_BY_FINGERPRINT))

        val cipher = cryptoObject.cipher
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(secretValue.iv))

        String(cipher.doFinal(secretValue.secret))
    } catch (ignored: Throwable) {
        null
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun prepareFingerprintCryptoObject(createKey: Boolean): FingerprintManager.CryptoObject {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        val key = if (createKey) {
            createFingerprintKey(ALIAS_FINGERPRINT, keyStore)
        } else {
            keyStore.getKey(ALIAS_FINGERPRINT, null) as SecretKey
        }

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return FingerprintManager.CryptoObject(cipher)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun createFingerprintKey(alias: String, keystore: KeyStore): SecretKey {
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
        return keyGenerator.generateKey()
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_SIZE)
        SecureRandom().nextBytes(salt)
        return salt
    }

    fun clear() {
        SettingsUtil.putString(noBackupSharedPref, KEY_SECURE_DATA_BY_PIN, SettingsUtil.EMPTY_STRING_SETTING)
        SettingsUtil.putString(noBackupSharedPref, KEY_SECURE_DATA_BY_FINGERPRINT, SettingsUtil.EMPTY_STRING_SETTING)
    }

    private class SecretValue(val secret: ByteArray, val iv: ByteArray, val salt: ByteArray) {
        companion object {
            private const val DELIMITER = "["

            fun fromString(value: String): SecretValue {
                val split = value.split(DELIMITER)
                if (split.size != 3) error("IllegalArgumentException while splitting value")

                return SecretValue(iv = decode(split[0]), salt = decode(split[1]), secret = decode(split[2]))
            }

            private fun decode(value: String): ByteArray = Base64.decode(value, Base64.NO_WRAP)

            private fun encode(value: ByteArray): String = Base64.encodeToString(value, Base64.NO_WRAP)
        }

        override fun toString(): String {
            return encode(iv) + DELIMITER + encode(salt) + DELIMITER + encode(secret)
        }
    }
}