package ru.surfstudio.standard.app.intialization.migration

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationStorage
import ru.surfstudio.android.dagger.scope.PerApplication

/**
 *
 */
@Module
class MigrationModule {

    @Provides
    @PerApplication
    fun provideAppMigrationStorageImpl(): AppMigrationStorage {
        return AppMigrationStorageImpl()
    }
}
