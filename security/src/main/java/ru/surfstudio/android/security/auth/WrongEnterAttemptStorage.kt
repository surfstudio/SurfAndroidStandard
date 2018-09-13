package ru.surfstudio.android.security.auth

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

/**
 * Хранилище попыток неправильного ввода пина и неудачного входа по отпечатку пальца
 */
@PerApplication
class WrongEnterAttemptStorage
@Inject constructor(@Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences) {

    companion object {
        private const val PIN_ATTEMPT_COUNT_KEY = "pin_attempt_count_key"
        private const val FINGERPRINT_ATTEMPT_COUNT_KEY = "fingerprint_attempt_count_key"
    }

    fun increasePinAttemptCount() = setPinAttemptCount(getPinAttemptCount() + 1)

    fun increaseFingerprintAttemptCount() = setFingerprintAttemptCount(getFingerprintAttemptCount() + 1)

    fun getPinAttemptCount(): Int = getInt(PIN_ATTEMPT_COUNT_KEY)

    fun getFingerprintAttemptCount(): Int = getInt(FINGERPRINT_ATTEMPT_COUNT_KEY)

    fun resetAttemptsCount() {
        setPinAttemptCount(0)
        setFingerprintAttemptCount(0)
    }

    private fun setPinAttemptCount(count: Int) = putInt(PIN_ATTEMPT_COUNT_KEY, count)

    private fun setFingerprintAttemptCount(count: Int) = putInt(FINGERPRINT_ATTEMPT_COUNT_KEY, count)

    private fun getInt(key: String): Int = SettingsUtil.getInt(noBackupSharedPref, key)

    private fun putInt(key: String, value: Int) = SettingsUtil.putInt(noBackupSharedPref, key, value)
}