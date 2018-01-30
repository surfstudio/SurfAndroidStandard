package ru.surfstudio.android.location.storage

import android.content.SharedPreferences
import ru.surfstudio.android.core.app.SharedPrefModule
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.util.SettingsUtil
import javax.inject.Inject
import javax.inject.Named

/**
 * Хранилище для подсчета количества закрытий баннера
 */
@PerApplication
class LocationDisabledStorage @Inject constructor(
        @Named(SharedPrefModule.NO_BACKUP_SHARED_PREF) val noBackupSharedPref: SharedPreferences) {

    private val KEY_BANNER_COUNT = "KEY_BANNER_COUNT"

    /**
     * Увеличивает счетчик на единицу
     */
    fun incBannerShowCount() {
        SettingsUtil.putInt(noBackupSharedPref, KEY_BANNER_COUNT, getBannerShowCount() + 1)
    }

    /**
     * Отдает количество закрытий баннера с запросом геолокации из кэша
     */
    fun getBannerShowCount() = SettingsUtil.getInt(noBackupSharedPref, KEY_BANNER_COUNT)
}