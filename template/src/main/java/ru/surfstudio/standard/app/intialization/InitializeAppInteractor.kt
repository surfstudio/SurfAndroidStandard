package ru.surfstudio.standard.app.intialization


import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationManager
import ru.surfstudio.android.core.domain.Unit
import rx.Observable
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
    fun initialize(): Observable<Unit> {
        return appMigrationManager.tryMigrateApp()
                .onErrorReturn { e -> Unit.INSTANCE }
    }

}
