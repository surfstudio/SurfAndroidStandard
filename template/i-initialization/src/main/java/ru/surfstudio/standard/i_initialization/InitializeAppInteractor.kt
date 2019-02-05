package ru.surfstudio.standard.i_initialization

import io.reactivex.Completable
import ru.surfstudio.android.app.migration.AppMigrationManager
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Инициализирует приложение
 */
@PerApplication
class InitializeAppInteractor @Inject constructor(
        private val appMigrationManager: AppMigrationManager
) {

    /**
     * инициализирует приложение
     *
     * @return observable, который всегда завершается успешно
     */
    fun initialize(): Completable {
        return appMigrationManager.tryMigrateApp()
    }
}