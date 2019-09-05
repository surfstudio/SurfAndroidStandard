package ru.surfstudio.android.app.migration.sample.app.initialization

import io.reactivex.Completable
import ru.surfstudio.android.app.migration.AppMigrationManager
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class InitializeAppInteractor @Inject constructor(private val appMigrationManager: AppMigrationManager) {

    /**
     * Инициализация приложения.
     * @return observable, который всегда завершается успешно
     */
    fun initialize(): Completable = appMigrationManager.tryMigrateApp()
}