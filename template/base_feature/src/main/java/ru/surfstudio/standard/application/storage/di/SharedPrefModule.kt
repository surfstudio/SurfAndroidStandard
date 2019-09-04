package ru.surfstudio.standard.application.storage.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.BACKUP_SHARED_PREF
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

@Module
class SharedPrefModule {

    @Provides
    @PerApplication
    @Named(NO_BACKUP_SHARED_PREF)
    fun provideNoBackupSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(NO_BACKUP_SHARED_PREF, Context.MODE_PRIVATE)
    }

    @Provides
    @PerApplication
    @Named(BACKUP_SHARED_PREF)
    fun provideBackupSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(BACKUP_SHARED_PREF, Context.MODE_PRIVATE)
    }
}