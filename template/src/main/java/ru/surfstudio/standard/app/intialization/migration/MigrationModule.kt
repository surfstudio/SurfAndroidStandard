package ru.surfstudio.standard.app.intialization.migration

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationStorage

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
