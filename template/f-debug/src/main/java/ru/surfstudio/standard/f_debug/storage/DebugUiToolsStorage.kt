package ru.surfstudio.standard.f_debug.storage

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

private const val IS_FPS_ENABLED_KEY = "IS_FPS_ENABLED_KEY"

@PerApplication
class DebugUiToolsStorage @Inject constructor(
        @Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences
) {
    var isFpsEnabled: Boolean
        get() = SettingsUtil.getBoolean(noBackupSharedPref, IS_FPS_ENABLED_KEY, false)
        set(value) = putBoolean(noBackupSharedPref, IS_FPS_ENABLED_KEY, value)
}