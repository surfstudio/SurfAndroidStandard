package ru.surfstudio.android.location.storage

import android.content.Context
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.util.SettingsUtil
import javax.inject.Inject

/**
 * Storage для хранения разрешенных Permissions
 */
@PerScreen
class LocationPermissionStorage @Inject constructor(val context: Context) {
    private val STORAGE_DIALOG_SHOW = "STORAGE_DIALOG_SHOW"

    fun getShowDialogValue(): Boolean {
        return SettingsUtil.getBoolean(context, STORAGE_DIALOG_SHOW, false)
    }

    fun saveShowDialogValue(show: Boolean) {
        SettingsUtil.putBoolean(context, STORAGE_DIALOG_SHOW, show)
    }
}