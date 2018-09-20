package ru.surfstudio.android.security.crypto

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import ru.surfstudio.android.filestorage.encryptor.Encryptor
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

class SecureEncryptorException(throwable: Throwable) : Throwable(throwable)

abstract class SecureEncryptor<T>(private val sign: T): Encryptor {

    override fun encrypt(decryptedBytes: ByteArray): ByteArray = try {
        val salt = SecurityUtils.generateSalt()
        val cipher = getCipher()

        SecretValue(cipher.doFinal(decryptedBytes), cipher.iv, salt).toBytes()
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }

    override fun decrypt(rawBytes: ByteArray): ByteArray = try {
        val encrypted = SecretValue.fromBytes(rawBytes)
        val cipher = getCipher()

        cipher.doFinal(encrypted.secret)
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }

    abstract fun getCipher(): Cipher
}

class SecurePinEncryptor(private val pin: String): Encryptor {

    override fun encrypt(decryptedBytes: ByteArray): ByteArray = try {
        val salt = SecurityUtils.generateSalt()
        val spec = SecurityUtils.getSpec(pin, salt)
        val cipher = SecurityUtils.getEncryptCipher(spec)

        SecretValue(cipher.doFinal(decryptedBytes), cipher.iv, salt).toBytes()
    }  catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }

    override fun decrypt(rawBytes: ByteArray): ByteArray = try {
        val encrypted = SecretValue.fromBytes(rawBytes)
        val spec = SecurityUtils.getSpec(pin, encrypted.salt)
        val cipher = SecurityUtils.getDecryptCipher(spec, encrypted.iv)

        cipher.doFinal(encrypted.secret)
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }
}

class SecureFingerPrintEncryptor(private val cryptoObject: FingerprintManager.CryptoObject): Encryptor {

    @TargetApi(Build.VERSION_CODES.M)
    override fun encrypt(decryptedBytes: ByteArray): ByteArray = try {
        val salt = SecurityUtils.generateSalt()
        val cipher = cryptoObject.cipher

        SecretValue(cipher.doFinal(decryptedBytes), cipher.iv, salt).toBytes()
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun decrypt(rawBytes: ByteArray): ByteArray = try {
        val encrypted = SecretValue.fromBytes(rawBytes)
        val cipher = cryptoObject.cipher
        cipher.init(Cipher.DECRYPT_MODE, SecurityUtils.getSecretKeyForFingerPrint(), IvParameterSpec(encrypted.iv))

        cipher.doFinal(encrypted.secret)
    } catch (throwable: Throwable) {
        throw SecureEncryptorException(throwable)
    }
}