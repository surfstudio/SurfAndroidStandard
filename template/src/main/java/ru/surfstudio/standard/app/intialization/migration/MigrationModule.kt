package ru.surfstudio.standard.app.intialization.migration

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.app.migration.AppMigrationStorage
import ru.surfstudio.android.app.migration.BaseAppMigrationModule
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.BuildConfig
import javax.inject.Named

/**
 *
 */
@Module
class MigrationModule : BaseAppMigrationModule() {

    @Provides
    @PerApplication
    fun provideAppMigrationStorageImpl(): AppMigrationStorage {
        return AppMigrationStorageImpl()
    }

    @Provides
    @PerApplication
    @Named(VERSION_CODE_PARAM)
    fun provideVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }
}
