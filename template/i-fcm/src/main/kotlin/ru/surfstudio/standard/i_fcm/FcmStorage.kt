package ru.surfstudio.standard.i_fcm

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

/**
 * Хранилище fcm-токена
 */
@PerApplication
class FcmStorage
@Inject constructor(@Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences) {

    companion object {
        private const val KEY_FCM_TOKEN = "FCM_TOKEN"
    }

    var fcmToken: String
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_FCM_TOKEN)
        set(value) = SettingsUtil.putString(noBackupSharedPref, KEY_FCM_TOKEN, value)

    fun clear() {
        SettingsUtil.putString(noBackupSharedPref, KEY_FCM_TOKEN, SettingsUtil.EMPTY_STRING_SETTING)
    }
}