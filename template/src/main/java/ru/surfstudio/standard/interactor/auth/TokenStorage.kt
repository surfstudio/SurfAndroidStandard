package ru.surfstudio.standard.interactor.auth

import android.content.SharedPreferences
import ru.surfstudio.android.core.app.SharedPrefModule
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.util.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

private const val KEY_AUTH_TOKEN = "AUTH_TOKEN"
private const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
private const val KEY_UNIQUE_ID = "UNIQUE_ID"

@PerApplication
class TokenStorage
@Inject
constructor(@Named(SharedPrefModule.NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences) {

    val authToken: String?
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_AUTH_TOKEN)

    val refreshToken: String?
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_REFRESH_TOKEN)

    val uniqueId: String
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_UNIQUE_ID)

    fun saveTokens(authToken: String, refreshToken: String) {
        SettingsUtil.putString(noBackupSharedPref, KEY_AUTH_TOKEN, authToken)
        SettingsUtil.putString(noBackupSharedPref, KEY_REFRESH_TOKEN, refreshToken)
    }


    fun clearTokens() {
        SettingsUtil.putString(noBackupSharedPref, KEY_AUTH_TOKEN, SettingsUtil.EMPTY_STRING_SETTING)
        SettingsUtil.putString(noBackupSharedPref, KEY_REFRESH_TOKEN, SettingsUtil.EMPTY_STRING_SETTING)
    }

    fun saveUniqueId(id: String) {
        SettingsUtil.putString(noBackupSharedPref, KEY_UNIQUE_ID, id)
    }

}
