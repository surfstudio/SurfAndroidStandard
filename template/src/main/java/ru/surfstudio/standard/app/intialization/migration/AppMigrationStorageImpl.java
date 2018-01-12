package ru.surfstudio.standard.app.intialization.migration;


import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.intialization.migration.AppMigration;
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationStorage;
import ru.surfstudio.android.core.app.log.Logger;

/**
 * хранилище {@link AppMigration}
 */
@PerApplication
public class AppMigrationStorageImpl implements AppMigrationStorage {

    private static final int MIGRATION_FOR_EACH = -1;
    private static final AppMigration[] MIGRATIONS = new AppMigration[]{
            //вызывается для каждого обновления
            new AppMigration(MIGRATION_FOR_EACH) {

                @Override
                public void execute(int oldVer, int newVer) {
                    Logger.d("Executing app migration for each update");
                    apply(); //нет проверки версий
                }

                @Override
                protected void apply() {
                    //здесь должен быть код миграции, который будет вызываться для каждого обновления
                }
            },

            new AppMigration(2) {
                @Override
                protected void apply() {
                    //здесь должен быть код миграции на версию 2
                }
            },

    };

    @Inject
    public AppMigrationStorageImpl() {
        //empty
    }

    @Override
    public AppMigration[] getMigrations() {
        return MIGRATIONS;
    }
}
