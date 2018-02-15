package ru.surfstudio.standard.app.intialization


import io.reactivex.Completable
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationManager
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

/**
 * Инициализирует приложение
 */
@PerApplication
public class InitializeAppInteractor @Inject
constructor(private val appMigrationManager: AppMigrationManager) {

    /**
     * инициализирует приложение
     *
     * @return observable, который всегда завершается успешно
     */
    fun initialize(): Completable {
        return appMigrationManager.tryMigrateApp()
    }

}
