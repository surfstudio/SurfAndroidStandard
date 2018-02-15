package ru.surfstudio.standard.interactor.auth

import android.content.SharedPreferences
import ru.surfstudio.android.core.app.SharedPrefModule
import ru.surfstudio.android.utilktx.util.SettingsUtil
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject
import javax.inject.Named

private const val KEY_AUTH_TOKEN = "AUTH_TOKEN"

@PerApplication
class TokenStorage @Inject constructor(
        @Named(SharedPrefModule.NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences
) {

    var authToken: String
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_AUTH_TOKEN)
        set(value) = SettingsUtil.putString(noBackupSharedPref, KEY_AUTH_TOKEN, value)

    fun clearTokens() {
        SettingsUtil.putString(noBackupSharedPref, KEY_AUTH_TOKEN, SettingsUtil.EMPTY_STRING_SETTING)
    }
}
