package ru.surfstudio.standard.i_onboarding

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

private const val KEY_ON_BOARDING = "on_boarding"

/**
 * Хранилище с флагами для показа экрана онбординга
 */
@PerApplication
class OnBoardingStorage @Inject constructor(
        @Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences
) {
    var shouldShowOnBoardingScreen: Boolean
        get() {
            return SettingsUtil.getBoolean(noBackupSharedPref, KEY_ON_BOARDING, true)
        }
        set(value) {
            SettingsUtil.putBoolean(noBackupSharedPref, KEY_ON_BOARDING, value)
        }
}