package ru.surfstudio.standard.f_debug.injector.ui.notification

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_push_notification.storage.FcmStorage
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named


@Module
class DebugFcmModule {

    @Provides
    @PerApplication
    fun provideFcmStorage(
            @Named(NO_BACKUP_SHARED_PREF) noBackupSharedPref: SharedPreferences
    ): FcmStorage {
        return FcmStorage(noBackupSharedPref)
    }
}