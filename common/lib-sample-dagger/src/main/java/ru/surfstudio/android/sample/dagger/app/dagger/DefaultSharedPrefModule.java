package ru.surfstudio.android.sample.dagger.app.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.dagger.scope.PerApplication;

import static ru.surfstudio.android.shared.pref.SettingsUtilKt.BACKUP_SHARED_PREF;
import static ru.surfstudio.android.shared.pref.SettingsUtilKt.NO_BACKUP_SHARED_PREF;

@Module
public class DefaultSharedPrefModule {

    @Provides
    @PerApplication
    @Named(NO_BACKUP_SHARED_PREF)
    SharedPreferences provideNoBackupSharedPref(Context context) {
        return context.getSharedPreferences(NO_BACKUP_SHARED_PREF, Context.MODE_PRIVATE);
    }

    @Provides
    @PerApplication
    @Named(BACKUP_SHARED_PREF)
    SharedPreferences provideBackupSharedPref(Context context) {
        return context.getSharedPreferences(BACKUP_SHARED_PREF, Context.MODE_PRIVATE);
    }
}
