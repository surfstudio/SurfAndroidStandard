package ru.surfstudio.android.security.auth

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

/**
 * Хранилище попыток неправильного ввода пина
 */
@PerApplication
class WrongEnterPinAttemptStorage
@Inject constructor(@Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences) {

    companion object {
        private const val ATTEMPT_COUNT_KEY = "attempt_count_key"
    }

    fun increaseAttemptCount() = setAttemptCount(getAttemptCount() + 1)

    fun getAttemptCount(): Int = SettingsUtil.getInt(noBackupSharedPref, ATTEMPT_COUNT_KEY)

    fun resetAttemptCount() = setAttemptCount(0)

    private fun setAttemptCount(count: Int) {
        SettingsUtil.putInt(noBackupSharedPref, ATTEMPT_COUNT_KEY, count)
    }
}