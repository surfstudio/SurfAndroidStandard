package ru.surfstudio.android.core.app;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.dagger.scope.PerApplication;

@Module
public class SharedPrefModule {

    public static final String NO_BACKUP_SHARED_PREF = "NO_BACKUP_SHARED_PREF";
    public static final String BACKUP_SHARED_PREF = "BACKUP_SHARED_PREF";

    @Provides
    @PerApplication
    @Named(SharedPrefModule.NO_BACKUP_SHARED_PREF)
    public SharedPreferences provideNoBackupSharedPref(Context context){
        return context.getSharedPreferences(NO_BACKUP_SHARED_PREF, Context.MODE_PRIVATE);
    }

    @Provides
    @PerApplication
    @Named(SharedPrefModule.BACKUP_SHARED_PREF)
    public SharedPreferences provideBackupSharedPref(Context context){
        return context.getSharedPreferences(BACKUP_SHARED_PREF, Context.MODE_PRIVATE);
    }
}
