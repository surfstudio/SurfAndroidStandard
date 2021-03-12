/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.app.migration;


import io.reactivex.Completable;
import ru.surfstudio.android.logger.Logger;

/**
 * Осуществляет миграцию приложения на новую версию
 */
public class AppMigrationManager {
    private final AppMigrationStorage appMigrationStorage;
    private final AppLaunchConfigurationStorage appLaunchConfiguration;
    private final long versionCode;

    @Deprecated()
    public AppMigrationManager(
            AppMigrationStorage appMigrationStorage,
            AppLaunchConfigurationStorage appLaunchConfiguration,
            int versionCode
    ) {
        this.appMigrationStorage = appMigrationStorage;
        this.appLaunchConfiguration = appLaunchConfiguration;
        this.versionCode = versionCode;
    }

    public AppMigrationManager(
            AppMigrationStorage appMigrationStorage,
            AppLaunchConfigurationStorage appLaunchConfiguration,
            long versionCode
    ) {
        this.appMigrationStorage = appMigrationStorage;
        this.appLaunchConfiguration = appLaunchConfiguration;
        this.versionCode = versionCode;
    }

    public Completable tryMigrateApp() {
        return Completable.fromRunnable(() -> {
            long currentVersion = versionCode;
            long lastLaunchVersion = appLaunchConfiguration.getLastLaunchVersion();
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
