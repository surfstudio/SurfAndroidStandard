package ru.surfstudio.standard.application.migration.di

import android.content.SharedPreferences
import ru.surfstudio.standard.base.intialization.migration.AppMigrationStorageImpl
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.app.migration.AppLaunchConfigurationStorage
import ru.surfstudio.android.app.migration.AppMigrationManager
import ru.surfstudio.android.app.migration.BuildConfig
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

/**
 * Модуль ддля предоставления зависимостей для миграции приложения
 */
@Module
class MigrationModule {

    @Provides
    @PerApplication
    internal fun provideAppLaunchConfigurationStorage(
            @Named(NO_BACKUP_SHARED_PREF) sharedPreferences: SharedPreferences
    ): AppLaunchConfigurationStorage {
        return AppLaunchConfigurationStorage(sharedPreferences)
    }

    @Provides
    @PerApplication
    internal fun provideAppMigrationManager(
            appLaunchConfigurationStorage: AppLaunchConfigurationStorage
    ): AppMigrationManager {
        return AppMigrationManager(
                AppMigrationStorageImpl(),
                appLaunchConfigurationStorage,
                BuildConfig.VERSION_CODE)
    }
}