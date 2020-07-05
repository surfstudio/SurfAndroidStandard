package ru.surfstudio.standard.i_token

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

private const val KEY_TOKEN = "TOKEN"

@PerApplication
class TokenStorage @Inject constructor(
        @Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences
) {

    var token: String
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_TOKEN)
        set(value) = SettingsUtil.putString(noBackupSharedPref, KEY_TOKEN, value)

    fun clearTokens() {
        SettingsUtil.putString(noBackupSharedPref, KEY_TOKEN, SettingsUtil.EMPTY_STRING_SETTING)
    }
}
