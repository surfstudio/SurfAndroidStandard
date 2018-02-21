package ru.surfstudio.android.app.migration;


import io.reactivex.Completable;
import ru.surfstudio.android.logger.Logger;

/**
 * Осуществляет миграцию приложения на новую версию
 */
public class AppMigrationManager {
    private final AppMigrationStorage appMigrationStorage;
    private final AppLaunchConfigurationStorage appLaunchConfiguration;
    private final int versionCode;

    public AppMigrationManager(AppMigrationStorage appMigrationStorage,
                               AppLaunchConfigurationStorage appLaunchConfiguration,
                               int versionCode) {
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
