/*
  Copyright (c) 2019-present, SurfStudio LLC. Margarita Volodina, Oleg Zhilo.

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
package ru.surfstudio.android.security.crypto.security

import android.util.Base64
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

private val HEX_ARRAY =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

object SecurityUtils {

    internal const val DEFAULT_CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding"
    internal const val DEFAULT_KEY_ALGORITHM = "PBKDF2WithHmacSHA1"

    private const val DEFAULT_MESSAGE_DIGEST_ALGORITHM_TYPE = "SHA-256"
    private const val DEFAULT_PUBLIC_KEY_ALGORITHM = "RSA"
    private const val DEFAULT_SALT_SIZE = 16

    /**
     * Function for generation of a random byte array
     */
    fun getRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    /**
     * Function for generation of a salt for encryption
     */
    fun generateSalt(saltSize: Int = DEFAULT_SALT_SIZE): ByteArray =
            getRandomBytes(saltSize)

    //region messageDigest
    /**
     * Function for a string data encryption with a given algorithm
     */
    fun messageDigest(
            data: String,
            algorithmType: String = DEFAULT_MESSAGE_DIGEST_ALGORITHM_TYPE
    ): ByteArray =
            messageDigest(data.toByteArray(), algorithmType)

    /**
     * Function for a byte array encryption with a given algorithm
     */
    fun messageDigest(
            data: ByteArray,
            algorithmType: String = DEFAULT_MESSAGE_DIGEST_ALGORITHM_TYPE
    ): ByteArray =
            MessageDigest.getInstance(algorithmType).digest(data)
    //endregion

    //region Base64
    /**
     * Function for a byte array encryption into Base64 format
     */
    fun encodeBase64(data: ByteArray): ByteArray = Base64.encode(data, Base64.NO_WRAP)

    /**
     * Function for a string data encryption into Base64 format
     */
    fun encodeBase64(data: String): ByteArray = encodeBase64(data.toByteArray())

    /**
     * Function for a string data encryption into Base64 format which returns a string data
     */
    fun encodeBase64ToString(data: String): String = String(encodeBase64(data.toByteArray()))

    /**
     * Function for a byte array decryption from Base64 format
     */
    fun decodeBase64(data: ByteArray): ByteArray = Base64.decode(data, Base64.NO_WRAP)

    /**
     * Function for a string data decryption from Base64 format
     */
    fun decodeBase64(data: String): ByteArray = decodeBase64(data.toByteArray())

    /**
     * Function for a string data decryption from Base64 format which returns a string data
     */
    fun decodeBase64ToString(data: String): String = String(decodeBase64(data.toByteArray()))
    //endregion

    /**
     * Function for a string data encryption with a given algorithm which returns a string data
     */
    fun hash(
            data: String,
            algorithmType: String = DEFAULT_MESSAGE_DIGEST_ALGORITHM_TYPE
    ): String =
            messageDigest(data, algorithmType)
                    .fold(EMPTY_STRING, { str, it -> str + "%02x".format(it) })

    /**
     * Function for a byte array conversion to hex-format
     */
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        bytes.indices.forEach {
            val current = bytes[it].toInt() and 0xFF
            hexChars[it * 2] = HEX_ARRAY[current.ushr(4)]
            hexChars[it * 2 + 1] = HEX_ARRAY[current and 0x0F]
        }
        return String(hexChars)
    }

    /**
     * Function which returns decrypted byte array data by given public key
     *
     * @param publicKeyStr public key, which was read from a .pem file
     * @param encryptedData encrypted by private key data
     * @param publicKeyAlgorithm algorithm which was used for public key generation
     */
    fun getDecryptedBytes(
            publicKeyStr: String,
            encryptedData: ByteArray,
            publicKeyAlgorithm: String = DEFAULT_PUBLIC_KEY_ALGORITHM
    ): ByteArray {
        val keySpec = X509EncodedKeySpec(decodeBase64(publicKeyStr))
        val publicKey = KeyFactory.getInstance(publicKeyAlgorithm).generatePublic(keySpec)

        with(Cipher.getInstance(publicKeyAlgorithm)) {
            init(Cipher.DECRYPT_MODE, publicKey)
            return doFinal(encryptedData)
        }
    }
}