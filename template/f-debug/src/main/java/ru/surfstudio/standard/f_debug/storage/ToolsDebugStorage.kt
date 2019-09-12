package ru.surfstudio.standard.f_debug.storage

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

private const val IS_STETHO_ENABLED_KEY = "IS_STETHO_ENABLED_KEY"

@PerApplication
class ToolsDebugStorage @Inject constructor(
        @Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences
) {
    var isStethoEnabled: Boolean
        get() = SettingsUtil.getBoolean(noBackupSharedPref, IS_STETHO_ENABLED_KEY, false)
        set(value) = putBoolean(noBackupSharedPref, IS_STETHO_ENABLED_KEY, value)
}