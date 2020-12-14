package ru.surfstudio.android.biometrics

import android.content.Context
import io.reactivex.Single
import io.reactivex.SingleEmitter
import ru.surfstudio.android.biometrics.encryptor.BiometricsEncryptorFactory
import ru.surfstudio.android.biometrics.encryptor.BiometricsKeyCreator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import java.security.Key
import javax.inject.Inject

/**
 * Service to work with biometrics scanner
 *
 * @param context applicationContext
 * @param keyCreator secret key creator
 * @param encryptorFactory fabric of the encryptor
 */
class BiometricsService @Inject constructor(
    context: Context,
    private val schedulersProvider: SchedulersProvider,
    private val keyCreator: BiometricsKeyCreator,
    private val encryptorFactory: BiometricsEncryptorFactory
) {

    private val biometricsCallback: BiometricsCallback =
        BiometricsCallback(context)

    /**
     * Starts process for scanning biometrics. On success scanning returns encrypted [data].
     * Should be called on main thread.
     *
     * @param data information to encrypt
     * @param screen screen where scanner dialog opening. Should be on of FragmentActivity or Fragment
     */
    fun <T> encryptByBiometrics(
        data: String,
        screen: T
    ): Single<String> {
        return observeBiometrics(true, screen)
            .observeOn(schedulersProvider.worker())
            .map { handleSetBiometricsResult(it, data) }
            .subscribeOn(schedulersProvider.main())
    }

    /**
     * Starts process for scanning biometrics. On success scanning returns decrypted [encryptedData].
     * Should be called on main thread.
     *
     * @param encryptedData encrypted data which returned from [encryptByBiometrics] method.
     * @param screen screen where scanner dialog opening. Should be on of FragmentActivity or Fragment
     */
    fun <T> decryptByBiometrics(
        encryptedData: String,
        screen: T
    ): Single<String> {
        return observeBiometrics(false, screen)
            .observeOn(schedulersProvider.worker())
            .map { handleCheckBiometricsResult(it, encryptedData) }
            .subscribeOn(schedulersProvider.main())
    }

    /**
     * Check for biometrics scanner
     */
    fun hasBiometricsScanner(): Boolean {
        return biometricsCallback.hasBiometricsScanner()
    }

    /**
     * Check for registered biometric data in device
     */
    fun hasBiometricsRegistered(): Boolean {
        return biometricsCallback.hasBiometricsRegistered()
    }

    private fun handleSetBiometricsResult(
        key: Key,
        data: String
    ): String {
        return String(encryptorFactory.getEncyptor(key).encrypt(data.toByteArray()))
    }

    private fun handleCheckBiometricsResult(key: Key, encryptedData: String): String {
        return String(encryptorFactory.getEncyptor(key).decrypt(encryptedData.toByteArray()))
    }

    private fun <T> observeBiometrics(
        createKey: Boolean = true,
        screen: T
    ): Single<Key> {
        return Single.create { emitter ->
            biometricsCallback.initFingerprintListener(
                BiometricListenerImpl(emitter, createKey),
                screen
            )
            emitter.setCancellable {
                biometricsCallback.stopListening()
            }
        }
    }

    private inner class BiometricListenerImpl(
        private val emitter: SingleEmitter<Key>,
        private val shouldCreateKey: Boolean
    ) : BiometricsListener {

        override fun onSuccess() {
            emitter.onSuccess(keyCreator.getSecretKey(shouldCreateKey))
        }

        override fun onError(biometricsException: Exception) {
            emitter.onError(biometricsException)
            Logger.e(biometricsException, biometricsException.message.toString())
        }
    }
}
