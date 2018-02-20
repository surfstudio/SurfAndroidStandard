package ru.surfstudio.android.app.migration;


import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import ru.surfstudio.android.dagger.scope.PerApplication;
import ru.surfstudio.android.logger.Logger;

/**
 * Осуществляет миграцию приложения на новую версию
 */
@PerApplication
public class AppMigrationManager {
    private final AppMigrationStorage appMigrationStorage;
    private final AppLaunchConfigurationStorage appLaunchConfiguration;
    private final int versionCode;

    @Inject
    public AppMigrationManager(AppMigrationStorage appMigrationStorage,
                               AppLaunchConfigurationStorage appLaunchConfiguration,
                               @Named(BaseAppMigrationModule.VERSION_CODE_PARAM) int versionCode) {
        this.appMigrationStorage = appMigrationStorage;
        this.appLaunchConfiguration = appLaunchConfiguration;
        this.versionCode = versionCode;
    }

    public Completable tryMigrateApp() {
        return Completable.fromRunnable(() -> {
            int currentVersion = versionCode;
            int lastLaunchVersion = appLaunchConfiguration.getLastLaunchVersion();
            if (lastLaunchVersion != currentVersion) {
                for (AppMigration migration : appMigrationStorage.getMigrations()) {
                    try {
                        migration.execute(lastLaunchVersion, currentVersion);
                    } catch (Exception e) {
                        //Перехватываем все исключения, тк неизвестно какая реализация у метода execute
                        Logger.e(e, "Migration with baseVersion " + migration.getBaseVersion() + " failed");
                    }
                }
                appLaunchConfiguration.setLaunchVersion(currentVersion);
            }
        });

    }
}
