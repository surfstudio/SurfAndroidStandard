package ru.surfstudio.android.shared.pref.sample.interactor.ip.cache

import android.content.SharedPreferences
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.SettingsUtil
import ru.surfstudio.android.shared.pref.sample.domain.ip.Ip
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject
import javax.inject.Named

/**
 * Хранилище для IP
 */
@PerApplication
class IpStorage @Inject constructor(
        @Named(NO_BACKUP_SHARED_PREF) private val noBackupSharedPref: SharedPreferences
) {

    companion object {
        private const val KEY_IP = "KEY_IP"
    }

    fun setIp(ip: Ip) {
        ipValue = ip.value
    }

    fun getIp(): Ip? = if (ipValue.isEmpty()) null else Ip(ipValue)

    fun clearIp() {
        ipValue = EMPTY_STRING
    }

    private var ipValue: String
        get() = SettingsUtil.getString(noBackupSharedPref, KEY_IP)
        set(value) = SettingsUtil.putString(noBackupSharedPref, KEY_IP, value)
}