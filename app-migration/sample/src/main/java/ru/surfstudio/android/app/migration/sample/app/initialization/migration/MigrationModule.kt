package ru.surfstudio.android.app.migration.sample.app.initialization.migration

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.app.migration.AppLaunchConfigurationStorage
import ru.surfstudio.android.app.migration.AppMigrationManager
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

/**
 * Модуль, предоставляющий зависимости для миграции приложения
 */
@Module
class MigrationModule {

    @PerApplication
    @Provides
    fun provideAppLaunchConfigurationStorage(
            @Named(NO_BACKUP_SHARED_PREF) sharedPreferences: SharedPreferences
    ): AppLaunchConfigurationStorage {
        return AppLaunchConfigurationStorage(sharedPreferences)
    }

    @PerApplication
    @Provides
    fun provideAppMigrationManager(
            appLaunchConfigurationStorage: AppLaunchConfigurationStorage,
            context: Context
    ): AppMigrationManager {
        return AppMigrationManager(
                AppMigrationStorageImpl(),
                appLaunchConfigurationStorage,
                getVersionCode(context)
        )
    }


    private fun getVersionCode(context: Context): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toLong()
        }
    }
}